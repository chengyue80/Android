/**
* Copyright (c) 2014 All rights reserved
* 名称：FieldInject.java
* 描述：TODO
* @author RC
* @date：2014-4-7 下午4:08:17
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
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldInject {


	int id() default -1;
	

	String type() default "";
}
