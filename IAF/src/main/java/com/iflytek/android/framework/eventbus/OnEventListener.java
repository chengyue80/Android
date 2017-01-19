package com.iflytek.android.framework.eventbus;

import com.iflytek.android.framework.eventbus.Event;

public class OnEventListener{
	public String eventName;
	
	public String listenerName;
	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getListenerName() {
		return listenerName;
	}

	public void setListenerName(String listenerName) {
		this.listenerName = listenerName;
	}

	/**
	 * @param event
	 * @return  是否继续事件
	 */
	public  Boolean doInBg(Event event){
		return false;
	};
	
	public Boolean doInUI(Event event){
		return false;
	};

	
}
