/**
 * Copyright (c) 2014 All rights reserved
 * 名称：BaseInject.java
 * 描述：TODO
 * @author wzgao
 * @date：2014-3-27 下午4:26:54
 * @version v1.0
 */
package com.iflytek.android.framework.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;

import com.iflytek.android.framework.base.AdapterViewListener;
import com.iflytek.android.framework.base.ViewListener;
import com.iflytek.android.framework.eventbus.EventBus;
import com.iflytek.android.framework.eventbus.InjectOnEventListener;
import com.iflytek.android.framework.eventbus.OnEventListener;
import com.iflytek.android.framework.util.StringUtils;

/**
 * @author wzgao
 * 
 */
public class BaseInject {

	/** BaseInject实例 */
	private static BaseInject INSTANCE = null;

	/** 获取BaseInject实例 */
	public synchronized static BaseInject getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BaseInject();
		}
		return INSTANCE;
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @throws
	 */
	public void initInject(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		Method[] methods = obj.getClass().getDeclaredMethods();
		int length = fields.length;
		Activity activity = null;
		if (obj instanceof Activity) {
			activity = (Activity) obj;
		} else if ((obj instanceof android.support.v4.app.Fragment))
			activity = ((android.support.v4.app.Fragment) obj).getActivity();
		else if (obj instanceof Fragment) {
			activity = ((Fragment) obj).getActivity();
		}
		if (fields != null && length > 0) {
			for (int i = 0; i < length; i++) {
				Field field = fields[i];
				try {
					// 可见性设置
					field.setAccessible(true);
					// 控件自动注解
					if (field.isAnnotationPresent(ViewInject.class)) {
						initView(obj, field, methods);
						// 类自动注解
					} else if (field.isAnnotationPresent(ClassInject.class)) {
						initClass(activity, field);
						// 资源自动注解
					} else if (field.isAnnotationPresent(ResourceInject.class)) {
						initResource(activity, field);
						// Bean自动注解
					} else if (field.isAnnotationPresent(BeanInject.class)) {
						Object object = initBean(activity, field);
						auto(object, field.getType(), activity);
					}
					//
					// else if (field.isAnnotationPresent(OnEvent.class)){
					// eventInject(obj);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 注入事件监听
	 * 
	 * @param obj
	 */
	public void eventInject(Object obj) {
		EventBus eventBus = EventBus.getInstance();
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			OnEvent onEvent = method.getAnnotation(OnEvent.class);
			if (onEvent == null)
				continue;
			OnEventListener listener = new InjectOnEventListener(obj, method,
					onEvent);
			if (!onEvent.onBefore()) {
				eventBus.clearEventTime(onEvent.name(), obj.getClass()
						.getSimpleName() + "." + method.getName());
			}
			eventBus.registerListener(onEvent.name(), obj.getClass()
					.getSimpleName() + "." + method.getName(), listener);
		}
	}

	public void unEventInject(Object obj) {
		EventBus eventBus = EventBus.getInstance();
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			OnEvent onEvent = method.getAnnotation(OnEvent.class);
			if (onEvent == null)
				continue;
			if (onEvent.autoUnRegist()) {
				eventBus.unregisterListener(onEvent.name(), obj.getClass()
						.getSimpleName() + "." + method.getName());
			}
		}
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param bean
	 * @param class1
	 * @param activity
	 * @throws
	 */
	public void auto(Object bean, Class<?> class1, Activity activity) {
		Field[] fields = class1.getDeclaredFields();
		View view = activity.getWindow().getDecorView();
		int length = fields.length;
		if (fields != null && length > 0) {
			for (int i = 0; i < length; i++) {
				Field field = fields[i];
				try {
					// 可见性设置
					field.setAccessible(true);
					FieldInject fieldInject = field
							.getAnnotation(FieldInject.class);
					if (field.isAnnotationPresent(FieldInject.class)) {
						int viewId = fieldInject.id();

						if (viewId == -1) {

							Field temp = Class.forName(
									activity.getPackageName() + ".R$id")
									.getField(field.getName());
							viewId = temp.getInt(temp);

						}
						field.set(bean, view.findViewById(viewId));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @param field
	 * @return
	 * @throws
	 */
	private Object initBean(Activity activity, Field field) {
		// TODO Auto-generated method stub
		BeanInject beanInject = field.getAnnotation(BeanInject.class);
		if (beanInject != null) {
			try {
				field.setAccessible(true);
				Object bean = field.getType().newInstance();
				field.set(activity, bean);
				return bean;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @param field
	 * @param methods
	 * @throws
	 */
	private void initView(Object obj, Field field, Method[] methods) {
		try {
			ViewInject baseInject = field.getAnnotation(ViewInject.class);
			if (baseInject != null) {

				int viewId = baseInject.id();
				View view = null;
				Activity activity = null;
				if (obj instanceof Activity) {
					activity = (Activity) obj;
					if (viewId == -1) {

						Field temp = Class.forName(
								activity.getPackageName() + ".R$id").getField(
								field.getName());
						viewId = temp.getInt(temp);

					}
					// 根据设定的资源ID绑定控件
					view = activity.getWindow().getDecorView()
							.findViewById(viewId);
				} else if ((obj instanceof android.support.v4.app.Fragment)) {
					activity = ((android.support.v4.app.Fragment) obj)
							.getActivity();
					if (viewId == -1) {
						Field temp = Class.forName(
								activity.getPackageName() + ".R$id").getField(
								field.getName());
						viewId = temp.getInt(temp);
					}
					// 根据设定的资源ID绑定控件
					view = ((android.support.v4.app.Fragment) obj).getView()
							.findViewById(viewId);
				} else if ((obj instanceof android.app.Fragment)) {
					activity = ((android.app.Fragment) obj).getActivity();
					if (viewId == -1) {
						Field temp = Class.forName(
								activity.getPackageName() + ".R$id").getField(
								field.getName());
						viewId = temp.getInt(temp);
					}
					// 根据设定的资源ID绑定控件
					view = ((android.app.Fragment) obj).getView().findViewById(
							viewId);
				}

				// // 实现绑定默认值
				// if(!StringUtils.isBlank(defaultValue)){
				// ((TextView)view).setText(defaultValue);
				// }
				field.set(obj, view);

				String fieldListenerName = baseInject.listenerName();
				String fieldMethodName = baseInject.methodName();
				if (StringUtils.isBlank(fieldListenerName)
						&& StringUtils.isBlank(fieldMethodName)) {
					HashMap<String, String[][]> map = new HashMap<String, String[][]>();
					for (int m = 0; m < methods.length; m++) {
						Method method = methods[m];
						if (method.isAnnotationPresent(ViewInject.class)) {
							ViewInject methodInject = method
									.getAnnotation(ViewInject.class);
							int methodId = methodInject.id();
							String listenerName = methodInject.listenerName();
							String methodName = methodInject.methodName();
							if (viewId == methodId && methodName != null
									&& listenerName != null) {
								if (map.containsKey(listenerName)) {
									String[][] array = map.get(listenerName);
									array[array.length + 1][0] = methodName;
									array[array.length + 1][1] = method
											.getName();
									map.put(listenerName, array);
								} else {
									map.put(listenerName, new String[][] { {
											methodName, method.getName() } });
								}
							}
						}
					}
					if (map.size() > 0) {
						Iterator<?> iter = map.entrySet().iterator();
						while (iter.hasNext()) {
							@SuppressWarnings("rawtypes")
							Map.Entry entry = (Map.Entry) iter.next();
							Object key = entry.getKey();
							Object val = entry.getValue();
							setListener(obj, field, (String[][]) val,
									(String) key);
						}
					}
				} else {
					setListener(obj, field, fieldMethodName, fieldListenerName);
				}

				SelectInject select = baseInject.select();
				if (!TextUtils.isEmpty(select.selected())) {
					setSelectListener(obj, field, select.selected(),
							select.noSelected());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @param field
	 * @throws
	 */
	private void initClass(Activity activity, Field field) {
		// TODO Auto-generated method stub
		try {
			field.setAccessible(true);
			field.set(activity, field.getType().newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @param field
	 * @throws
	 */
	private void initResource(Activity activity, Field field) {
		if (field.isAnnotationPresent(ResourceInject.class)) {
			ResourceInject resourceJect = field
					.getAnnotation(ResourceInject.class);
			int resourceID = resourceJect.id();
			try {
				field.setAccessible(true);
				Resources resources = activity.getResources();
				String type = resources.getResourceTypeName(resourceID);
				if (type.equalsIgnoreCase("string")) {
					field.set(activity, resources.getString(resourceID));
				} else if (type.equalsIgnoreCase("drawable")) {
					field.set(activity, resources.getDrawable(resourceID));
				} else if (type.equalsIgnoreCase("layout")) {
					field.set(activity, resources.getLayout(resourceID));
				} else if (type.equalsIgnoreCase("array")) {
					if (field.getType().equals(int[].class)) {
						field.set(activity, resources.getIntArray(resourceID));
					} else if (field.getType().equals(String[].class)) {
						field.set(activity,
								resources.getStringArray(resourceID));
					} else {
						field.set(activity,
								resources.getStringArray(resourceID));
					}

				} else if (type.equalsIgnoreCase("color")) {
					if (field.getType().equals(Integer.TYPE)) {
						field.set(activity, resources.getColor(resourceID));
					} else {
						field.set(activity,
								resources.getColorStateList(resourceID));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @param field
	 * @param selected
	 * @param noSelected
	 * @throws Exception
	 * @throws
	 */
	private void setSelectListener(Object activity, Field field,
			String selected, String noSelected) throws Exception {
		Object obj = field.get(activity);
		((AbsListView) obj).setOnItemSelectedListener(new AdapterViewListener(
				activity, selected, noSelected));
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @param field
	 * @param methodName
	 * @param listenerName
	 * @throws Exception
	 * @throws
	 */
	private void setListener(Object activity, Field field,
			String[][] methodName, String listenerName) throws Exception {
		if (methodName != null && methodName.length > 0) {

			Object obj = field.get(activity);
			if (obj instanceof View) {
				if ("onClick".equals(listenerName)) {
					((View) obj).setOnClickListener(new ViewListener(activity,
							methodName));
				} else if ("onLongClick".equals(listenerName)) {
					((View) obj).setOnLongClickListener(new ViewListener(
							activity, methodName));
				} else if ("onTouch".equals(listenerName)) {
					((View) obj).setOnTouchListener(new ViewListener(activity,
							methodName));
				} else if ("onSystemUiVisibilityChange".equals(listenerName)) {
					((View) obj)
							.setOnSystemUiVisibilityChangeListener(new ViewListener(
									activity, methodName));
				} else if ("onKey".equals(listenerName)) {
					((View) obj).setOnKeyListener(new ViewListener(activity,
							methodName));
				} else if ("onHover".equals(listenerName)) {
					((View) obj).setOnHoverListener(new ViewListener(activity,
							methodName));
				} else if ("onGenericMotion".equals(listenerName)) {
					((View) obj).setOnGenericMotionListener(new ViewListener(
							activity, methodName));
				} else if ("onFocusChange".equals(listenerName)) {
					((View) obj).setOnFocusChangeListener(new ViewListener(
							activity, methodName));
				} else if ("onDrag".equals(listenerName)) {
					((View) obj).setOnDragListener(new ViewListener(activity,
							methodName));
				} else if ("onCreateContextMenu".equals(listenerName)) {
					((View) obj)
							.setOnCreateContextMenuListener(new ViewListener(
									activity, methodName));
				} else if ("onViewAttachedToWindow".equals(listenerName)) {
					((View) obj)
							.addOnAttachStateChangeListener(new ViewListener(
									activity, methodName));
				} else if ("onLayoutChange".equals(listenerName)) {
					((View) obj).addOnLayoutChangeListener(new ViewListener(
							activity, methodName));
				}

			} else if (obj instanceof AbsListView) {
				if ("onItemClick".equals(listenerName)) {
					((AbsListView) obj)
							.setOnItemClickListener(new AdapterViewListener(
									activity, methodName));
				} else if ("onItemLongClick".equals(listenerName)) {
					((AbsListView) obj)
							.setOnItemLongClickListener(new AdapterViewListener(
									activity, methodName));
				} else if ("onScrollListener".equals(listenerName)) {
					((AbsListView) obj)
							.setOnScrollListener(new AdapterViewListener(
									activity, methodName));
				}
			}

		}
	}

	/**
	 * 
	 * 描述：TODO
	 * 
	 * @param activity
	 * @param field
	 * @param methodName
	 * @param listenerName
	 * @throws Exception
	 * @throws
	 */
	private void setListener(Object activity, Field field, String methodName,
			String listenerName) throws Exception {
		if (methodName != null && methodName.trim().length() > 0) {

			Object obj = field.get(activity);
			if (obj instanceof View) {
				if ("onClick".equals(listenerName)) {
					((View) obj).setOnClickListener(new ViewListener(activity,
							methodName));
				} else if ("onLongClick".equals(listenerName)) {
					((View) obj).setOnLongClickListener(new ViewListener(
							activity, methodName));
				} else if ("onTouch".equals(listenerName)) {
					((View) obj).setOnTouchListener(new ViewListener(activity,
							methodName));
				} else if ("onSystemUiVisibilityChange".equals(listenerName)) {
					((View) obj)
							.setOnSystemUiVisibilityChangeListener(new ViewListener(
									activity, methodName));
				} else if ("onKey".equals(listenerName)) {
					((View) obj).setOnKeyListener(new ViewListener(activity,
							methodName));
				} else if ("onHover".equals(listenerName)) {
					((View) obj).setOnHoverListener(new ViewListener(activity,
							methodName));
				} else if ("onGenericMotion".equals(listenerName)) {
					((View) obj).setOnGenericMotionListener(new ViewListener(
							activity, methodName));
				} else if ("onFocusChange".equals(listenerName)) {
					((View) obj).setOnFocusChangeListener(new ViewListener(
							activity, methodName));
				} else if ("onDrag".equals(listenerName)) {
					((View) obj).setOnDragListener(new ViewListener(activity,
							methodName));
				} else if ("onCreateContextMenu".equals(listenerName)) {
					((View) obj)
							.setOnCreateContextMenuListener(new ViewListener(
									activity, methodName));
				} else if ("onViewAttachedToWindow".equals(listenerName)) {
					((View) obj)
							.addOnAttachStateChangeListener(new ViewListener(
									activity, methodName));
				} else if ("onLayoutChange".equals(listenerName)) {
					((View) obj).addOnLayoutChangeListener(new ViewListener(
							activity, methodName));
				}

			} else if (obj instanceof AbsListView) {
				if ("onItemClick".equals(listenerName)) {
					((AbsListView) obj)
							.setOnItemClickListener(new AdapterViewListener(
									activity, methodName));
				} else if ("onItemLongClick".equals(listenerName)) {
					((AbsListView) obj)
							.setOnItemLongClickListener(new AdapterViewListener(
									activity, methodName));
				} else if ("onScrollListener".equals(listenerName)) {
					((AbsListView) obj)
							.setOnScrollListener(new AdapterViewListener(
									activity, methodName));
				}
			}

		}
	}
}
