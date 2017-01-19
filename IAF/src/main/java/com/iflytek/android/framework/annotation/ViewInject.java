/**
 * Copyright (c) 2012 All rights reserved
 * ��ƣ�BaseInject.java
 * ������TODO
 * @author wzgao
 * @date��2014-3-25 ����2:05:06
 * @version v1.0
 */
package com.iflytek.android.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wzgao
 * 
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {

	/** UI ID */
	public int id() default -1;

	/** listenerName */
	public String listenerName() default "";

	/** methodName */
	public String methodName() default "";

	/** methodName */
	public SelectInject select() default @SelectInject(selected = "");
	
	/** 控件默认值 */
	public String defaultValue() default "";
}
