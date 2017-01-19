package com.iflytek.android.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * AssetDatabaseOpenHelper
 * 
 * 自动备份数据库到/data/data/package_name/databases
 */
public class AssetDatabaseOpenHelper {

	private Context context;
	private String databaseName;

	public AssetDatabaseOpenHelper(Context context, String databaseName) {
		this.context = context;
		this.databaseName = databaseName;
	}

	/**
	 * 创建数据库连接
	 * 
	 * @return
	 * @throws RuntimeException
	 *             不能备份数据库
	 * @throws SQLiteException
	 *             数据库无法打开
	 */
	public synchronized SQLiteDatabase getWritableDatabase() {
		File dbFile = context.getDatabasePath(databaseName);
		if (dbFile != null && !dbFile.exists()) {
			try {
				copyDatabase(dbFile);
			} catch (IOException e) {
				throw new RuntimeException("Error creating source database", e);
			}
		}

		return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * 创建和打开个只读的数据库
	 * 
	 * @return
	 * @throws RuntimeException
	 *             不能备份数据库
	 * @throws SQLiteException
	 *             数据库无法打开
	 */
	public synchronized SQLiteDatabase getReadableDatabase() {
		File dbFile = context.getDatabasePath(databaseName);
		if (dbFile != null && !dbFile.exists()) {
			try {
				copyDatabase(dbFile);
			} catch (IOException e) {
				throw new RuntimeException("Error creating source database", e);
			}
		}

		return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
				SQLiteDatabase.OPEN_READONLY);
	}

	/**
	 * @return the database name
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * 
	 * 描述：TODO 复制数据库文件
	 * @param dbFile
	 * @throws IOException
	 * @throws
	 */
	private void copyDatabase(File dbFile) throws IOException {
		InputStream stream = context.getAssets().open(databaseName);
		FileUtils.writeFile(dbFile, stream);
		stream.close();
	}
}
