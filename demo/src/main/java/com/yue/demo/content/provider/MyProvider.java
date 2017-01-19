package com.yue.demo.content.provider;
//package org.yue.mytest.content.provider;
//
//import org.yue.mytest.Mlog;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.net.Uri;
//
///**
// * 
// * @author chengyue
// * 
// */
//public class MyProvider extends ContentProvider {
//
//	// 第一次创建该ContentProvider时调用该方法
//	@Override
//	public boolean onCreate() {
//		Mlog.i("===onCreate方法被调用===");
//		return true;
//	}
//
//	// 该方法的返回值代表了该ContentProvider所提供数据的MIME类型
//	@Override
//	public String getType(Uri uri) {
//		Mlog.i("===getType方法被调用===");
//		return null;
//	}
//
//	// 实现查询方法，该方法应该返回查询得到的Cursor
//	@Override
//	public Cursor query(Uri uri, String[] projection, String where,
//			String[] whereArgs, String sortOrder) {
//		Mlog.i(uri + "===query方法被调用===");
//		Mlog.i("where参数为：" + where);
//		return null;
//	}
//
//	// 实现插入的方法，该方法应该新插入的记录的Uri
//	@Override
//	public Uri insert(Uri uri, ContentValues values) {
//		Mlog.i(uri + "===insert方法被调用===");
//		Mlog.i("values参数为：" + values);
//		return null;
//	}
//
//	// 实现删除方法，该方法应该返回被删除的记录条数
//	@Override
//	public int delete(Uri uri, String where, String[] whereArgs) {
//		Mlog.i(uri + "===delete方法被调用===");
//		Mlog.i("where参数为：" + where);
//		return 0;
//	}
//
//	// 实现删除方法，该方法应该返回被更新的记录条数
//	@Override
//	public int update(Uri uri, ContentValues values, String where,
//			String[] whereArgs) {
//		Mlog.i(uri + "===update方法被调用===");
//		Mlog.i("where参数为：" + where + ",values参数为：" + values);
//		return 0;
//	}
// }