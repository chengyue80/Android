/**
 * Copyright (c) 2014 All rights reserved
 * 名称：ClassUtils.java
 * 描述：TODO
 * @author zlchen
 * @date：2014年4月2日 下午8:21:02
 * @version v1.0
 */
package com.iflytek.android.framework.util;

import java.lang.reflect.Method;

/**
 * @author wzgao
 * 
 */
public class ClassUtils {

	public static Class<?>[] getClass(Class<?>... params) {
		return params;
	}

	/**
	 * 
	 * 描述：执行控件动作
	 * @param handler
	 * @param methodName 方法名称
	 * @param classs 类名称
	 * @param params 方法参数
	 * @return
	 * @throws
	 */
	public static boolean invokeClickMethod(Object handler, String methodName,
			Class<?>[] classs, Object... params) {
		if (handler != null) {
			Method method = null;
			try {
				method = handler.getClass().getDeclaredMethod(methodName,
						classs);
				if (method != null) {
					Object obj = method.invoke(handler, params);
					return obj == null ? false : Boolean
							.valueOf(obj.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 描述：执行控件动作
	 * @param handler
	 * @param methodName 方法名称
	 * @param classs 类名称
	 * @param params 方法参数
	 * @return
	 * @throws
	 */
	public static boolean invokeClickMethod(Class<?> cl, String methodName,
			Class<?>[] classs, Object... params) {
		if (cl != null) {
			Method method = null;
			try {
				method = cl.getDeclaredMethod(methodName,
						classs);
				if (method != null) {
					Object obj = method.invoke(cl.newInstance(), params);
					return obj == null ? false : Boolean
							.valueOf(obj.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
