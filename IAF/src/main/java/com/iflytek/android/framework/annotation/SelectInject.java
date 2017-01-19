/**
 * Copyright (c) 2014 All rights reserved
 * 名称：SelectInject.java
 * 描述：TODO
 * @author wzgao
 * @date：2014-3-26 下午4:15:04
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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) 
public @interface SelectInject {

	public String selected();
	public String noSelected() default "";
}
