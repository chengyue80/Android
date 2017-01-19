package com.iflytek.android.framework.db;

/**
 * 数据库监听
 * 
 * @author chengyue
 * @version v1.0.5
 * @since JDK 1.6
 */
public abstract interface DBHelpListener {

	/**
	 * 数据库创建
	 * 
	 * @author chengyue
	 * @since JDK 1.6
	 */
	public abstract void onCreate();

	/**
	 * 删除重建数据库前操作
	 * 
	 * @author chengyue
	 */
	public abstract void onUpGradeBefore();

	/**
	 * 删除重建数据库后操作
	 * 
	 * @author chengyue
	 */
	public abstract void onUpGradeOver();
}