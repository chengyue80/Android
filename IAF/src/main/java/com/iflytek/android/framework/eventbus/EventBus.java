package com.iflytek.android.framework.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iflytek.android.framework.base.BaseApplication;
import com.iflytek.android.framework.thread.Task;
import com.iflytek.android.framework.thread.ThreadWorker;
import com.iflytek.android.framework.util.PreferencesUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 时间总线管理器
 * @author chenzhilei
 *
 */
public class EventBus {

	Map<String, EventQueue> eventQueues = new HashMap<String, EventQueue>();
	Map<String, List<OnEventListener>> eventListeners = new HashMap<String, List<OnEventListener>>();
	Map<String, OnEventListener> Listeners = new HashMap<String, OnEventListener>();
	
    /** EventBus实例 */
    private static EventBus INSTANCE = null;

    /** 获取EventBus实例 */
    public synchronized static EventBus getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventBus();
        }
        return INSTANCE;
    }
	
	

	/**
	 * 清空事件
	 * 
	 * @param name
	 */
	public void clearEvents(String name) {
		EventQueue queue = eventQueues.get(name);
		if (queue == null) {
			queue = new EventQueue();
			eventQueues.put(name, queue);
		}
		queue.clearEvents();
	}

	/**
	 * 发布事件
	 */
	public void fireEvent(String name, Object... params) {
		Event event = new Event();
		event.setName(name);
		event.setEventTime(System.currentTimeMillis());
		event.setParams(params);
		fireEvent(event);
	}

	/**
	 * 发布事件
	 */
	public void fireEvent(final Event event) {
		if (event != null) {
			EventQueue queue = eventQueues.get(event.getName());
			if (queue == null) {
				queue = new EventQueue();
				eventQueues.put(event.getName(), queue);
			}
			queue.addEvent(event);
		}

		final String eventname = event.getName();
		// 在主线程里
		if (Looper.myLooper() == Looper.getMainLooper()) {
			ThreadWorker.execuse(false, new Task(BaseApplication.iafContext) {
				public void doInBackground() {
					super.doInBackground();
					List<OnEventListener> list = eventListeners.get(eventname);
					for (int i = 0; list != null && i < list.size(); i++) {
						OnEventListener listener = list.get(i);
						listener.doInBg(event);
					}
				}

				public void doInUI(Object obj, Integer what) {
					List<OnEventListener> list = eventListeners.get(eventname);
					for (int i = 0; list != null && i < list.size(); i++) {
						OnEventListener listener = list.get(i);
						listener.doInUI(event);
						PreferencesUtils.putLong(BaseApplication.iafContext, eventname+listener.getListenerName(), System.currentTimeMillis());
					}
				}
			});
		} else {
			List<OnEventListener> list = eventListeners.get(eventname);
			for (int i = 0; list != null && i < list.size(); i++) {
				OnEventListener listener = list.get(i);
				listener.doInBg(event);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("listener", listener);
				map.put("event", event);
				handler.sendMessage(handler.obtainMessage(0x10000, map));
			}
		}
	}

	/**
	 * 事件监听
	 * 
	 * @param name
	 * @param listener
	 * @param queue
	 */
	public void registerListener(final String name, final String listenerName,
			final OnEventListener listener) {
		String key = name + "#" + listenerName;
		OnEventListener oldlis = Listeners.get(key);
		if (oldlis != null) {
			// 防止重复注册相同名字的监听器
			unregisterListener(name, oldlis, key);
		}
		listener.setEventName(name);
		listener.setListenerName(listenerName);
		List<OnEventListener> listeners = eventListeners.get(name);
		if (listeners == null) {
			listeners = new ArrayList<OnEventListener>();
			eventListeners.put(name, listeners);
		}
		listeners.add(listener);
		Listeners.put(key, listener);
		// 触发已发生的时间
		ThreadWorker.execuse(false, new Task(BaseApplication.iafContext) {
			@Override
			public void doInBackground() {
				super.doInBackground();
				EventQueue queue = eventQueues.get(name);
				if (queue == null)
					return;
				
				Long time = PreferencesUtils.getLong(BaseApplication.iafContext ,name + listenerName, 0);
				List<Event> events = queue.getEvents(time);
				if (events != null) {
					for (int i = 0; i < events.size(); i++) {
						Event event = events.get(i);
						if (!listener.doInBg(event)) {
							break;
						}
					}
				}
			}

			@Override
			public void doInUI(Object obj, Integer what) {
				EventQueue queue = eventQueues.get(name);
				if (queue == null)
					return;
				Long time = PreferencesUtils.getLong(BaseApplication.iafContext ,name + listenerName, 0);
				List<Event> events = queue.getEvents(time);
				if (events != null) {
					for (int i = 0; i < events.size(); i++) {
						Event event = events.get(i);
						if (!listener.doInUI(event)) {
							break;
						}
					}
				}
				//更新时间
				PreferencesUtils.putLong(BaseApplication.iafContext,name + listenerName,
								System.currentTimeMillis());
			}
		});
	}

	/**
	 * 移除监听
	 * 
	 * @param eventName
	 * @param listenerName
	 */
	public void unregisterListener(String eventName, String listenerName) {
		String key = eventName + "#" + listenerName;
		OnEventListener listener = Listeners.get(key);
		unregisterListener(eventName, listener , key);
	}

	/**
	 * 更新事件时间为最新时间
	 * 
	 * @param eventName
	 * @param listenerName
	 */
	public void clearEventTime(String eventName, String listenerName) {
		PreferencesUtils.putLong(BaseApplication.iafContext,eventName + listenerName, System.currentTimeMillis());
	}

	/**
	 * 移除监听
	 * 
	 * @param eventName
	 * @param listerName
	 */
	public void unregisterListener(String eventName, OnEventListener listener ,String key) {
		List<OnEventListener> lis = eventListeners.get(eventName);
		lis.remove(listener);
		Listeners.remove(key);
	}

	static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x10000 && msg.obj != null
					&& msg.obj instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				OnEventListener listener = (OnEventListener) map
						.get("listener");
				Event event = (Event) map.get("event");
				listener.doInUI(event);
				PreferencesUtils.putLong( BaseApplication.iafContext,listener.getListenerName(),
								System.currentTimeMillis());
			}
		}
	};
}
