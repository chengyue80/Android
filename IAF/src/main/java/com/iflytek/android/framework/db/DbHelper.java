package com.iflytek.android.framework.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * 
 * @author chengyue
 * @version v1.0.4
 * @changeTime 2015-12-01<br\>
 *             修复Android5.0系统中无法在数据库升级的onUpgrade方法中进行数据库删除的bug.
 * 
 */
public class DbHelper {

	/**
	 * 数据库类
	 */
	private SQLiteDatabase db;
	/**
	 * context
	 */
	Context ctx;

	/** 数据库监听 */
	private DBHelpListener mDbHelpListener;

	/** 数据库是否升级 */
	private boolean isNeedUpgrade = false;

	/**
	 * 构造方法初始化
	 * 
	 * @param context
	 */

	public DbHelper(Context context) {
		this.ctx = context;
	}

	/**
	 * 在应用内创建数据库
	 * 
	 * @param dbname
	 * @param dbversion
	 */
	public void init(String dbname, int dbversion) {
		init(dbname, dbversion, null);
	};

	/**
	 * 在应用内创建数据库
	 * 
	 * @param dbname
	 * @param dbversion
	 * @param helpListener
	 * @change 修改handle message 为监听器DBHelpListener
	 */
	public void init(String dbname, int dbversion, DBHelpListener helpListener) {
		if (helpListener == null) {
			this.mDbHelpListener = new DBHelpListener() {

				@Override
				public void onUpGradeOver() {

				}

				@Override
				public void onUpGradeBefore() {

				}

				@Override
				public void onCreate() {

				}
			};
		} else {
			this.mDbHelpListener = helpListener;
		}

		this.db = new SqliteDbHelper(this.ctx, dbname, dbversion)
				.getWritableDatabase();
		if (this.isNeedUpgrade) {
			this.mDbHelpListener.onUpGradeBefore();
			this.ctx.deleteDatabase(dbname);
			this.db = new SqliteDbHelper(this.ctx, dbname, dbversion)
					.getWritableDatabase();
			this.mDbHelpListener.onUpGradeOver();
		}
	}

	/**
	 * 保存
	 * 
	 * @param obj
	 */
	public void save(Object obj) {
		if (obj == null)
			return;
		checkOrCreateTable(obj.getClass());
		SqlProxy proxy = SqlProxy.insert(obj);
		db.execSQL(proxy.getSql(), proxy.paramsArgs());
	}

	/**
	 * 更新
	 * 
	 * @param obj
	 */
	public void update(Object obj) {
		if (obj == null)
			return;
		checkOrCreateTable(obj.getClass());
		SqlProxy proxy = SqlProxy.update(obj);
		db.execSQL(proxy.getSql(), proxy.paramsArgs());
	}

	/**
	 * 单个结果集查询
	 * 
	 * @param proxy
	 * @return
	 */
	public <T> T queryFrist(Class<T> clazz, String where, Object... whereargs) {
		if (where.indexOf("limit") < -1) {
			where += " limit 0,1";
		}
		List<T> list = queryList(clazz, where, whereargs);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	/**
	 * 通过sql查询多个结果集
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> List<T> queryList(Class<T> clazz, String where,
			Object... whereargs) {
		checkOrCreateTable(clazz);
		SqlProxy proxy = SqlProxy.select(clazz, where, whereargs);
		return queryList(proxy);
	}

	/**
	 * 查询列表总数 当结果为-1的时候说明查询条件不成立或表不存在，结果异常
	 * 
	 * @param <T>
	 * @param clazz
	 * @param where
	 * @param whereargs
	 * @return
	 */
	public <T> long queryCount(Class<T> clazz, String where,
			Object... whereargs) {
		checkOrCreateTable(clazz);
		SqlProxy proxy = SqlProxy.selectCount(clazz, where, whereargs);
		return queryCount(proxy);
	}

	/**
	 * 查询单个结果集
	 * 
	 * @param proxy
	 * @return
	 */
	public <T> T queryFrist(SqlProxy proxy) {
		String sql = proxy.getSql();
		if (sql.indexOf("limit") < -1) {
			sql += " limit 0,1";
			proxy = SqlProxy.select(proxy.getRelClass(), sql,
					(Object) proxy.paramsArgs());
		}
		List<T> list = queryList(proxy);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询多个结果集
	 * 
	 * @param proxy
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryList(SqlProxy proxy) {
		Cursor cursor = db.rawQuery(proxy.getSql(), proxy.paramsArgs());
		try {
			List<T> list = new ArrayList<T>();
			while (cursor.moveToNext()) {
				T t = (T) cursorToBean(cursor, proxy.getRelClass());
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			cursor = null;
		}
		return null;
	}

	/**
	 * 查询列表总数
	 * 
	 * @param proxy
	 * @return
	 */
	public long queryCount(SqlProxy proxy) {
		Cursor cursor = db.rawQuery(proxy.getSql(), proxy.paramsArgs());
		try {
			cursor.moveToFirst();
			return cursor.getLong(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			cursor = null;
		}
		return -1;
	}

	/**
	 * 获取最近一次插入的主键
	 * 
	 * @return
	 */
	public Long getLastInsertId(String table) {
		Cursor c = db.rawQuery("select Max(id) from " + table, null);
		Long count = 0L;
		if (c.moveToNext()) {
			count = c.getLong(0);
		}
		c.close();
		return count;
	}

	/**
	 * 对象封装
	 * 
	 * @param cursor
	 * @param clazz
	 * @return
	 */
	private <T> T cursorToBean(Cursor cursor, Class<T> clazz) {
		EntityInfo entity = EntityInfo.build(clazz);
		Set<String> keys = entity.getColumns().keySet();
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		// 反射获取值
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			String column = entity.getColumns().get(key);
			Field field = BeanUtil.getDeclaredField(clazz, key);
			if (field.getType().equals(Integer.class)
					|| field.getType().equals(int.class)) {
				BeanUtil.setProperty(obj, key,
						cursor.getInt(cursor.getColumnIndex(column)));
			} else if (field.getType().equals(Long.class)
					|| field.getType().equals(long.class)) {
				BeanUtil.setProperty(obj, key,
						cursor.getLong(cursor.getColumnIndex(column)));
			} else if (field.getType().equals(Double.class)
					|| field.getType().equals(double.class)) {
				BeanUtil.setProperty(obj, key,
						cursor.getDouble(cursor.getColumnIndex(column)));
			} else if (field.getType().equals(Float.class)
					|| field.getType().equals(float.class)) {
				BeanUtil.setProperty(obj, key,
						cursor.getFloat(cursor.getColumnIndex(column)));
			} else if (field.getType().equals(String.class)) {
				BeanUtil.setProperty(obj, key,
						cursor.getString(cursor.getColumnIndex(column)));
			} else if (field.getType().equals(Date.class)) {
				try {
					BeanUtil.setProperty(
							obj,
							key,
							new Date(cursor.getLong(cursor
									.getColumnIndex(column))));
				} catch (Exception e) {
				}
			} else if (field.getType().equals(Boolean.class)
					|| field.getType().equals(boolean.class)) {
				BeanUtil.setProperty(obj, key, cursor.getInt(cursor
						.getColumnIndex(column)) == 0 ? false : true);
			}
		}

		return obj;
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	public void checkOrCreateTable(Class clazz) {
		EntityInfo info = EntityInfo.build(clazz);
		if (info.isChecked()) {
			return;
		} else {
			boolean isexit = checkTable(info.table);
			if (!isexit) {
				String sql = getCreatTableSQL(clazz);
				db.execSQL(sql);
			}
		}
	}

	/**
	 * 获取表创建语句
	 * 
	 * @param clazz
	 * @return
	 */
	private static String getCreatTableSQL(Class<?> clazz) {
		EntityInfo info = EntityInfo.build(clazz);
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ");
		sql.append(info.getTable());
		sql.append(" ( ");
		Map<String, String> propertys = info.getColumns();
		Set<String> keys = propertys.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			sql.append(propertys.get(key));
			Class<?> dataType = BeanUtil.getDeclaredField(clazz, key).getType();
			if (dataType == int.class || dataType == Integer.class
					|| dataType == long.class || dataType == Long.class) {
				sql.append(" INTEGER");
			} else if (dataType == float.class || dataType == Float.class
					|| dataType == double.class || dataType == Double.class) {
				sql.append(" REAL");
			} else if (dataType == boolean.class || dataType == Boolean.class) {
				sql.append(" NUMERIC");
			}
			if (key.equals(info.pk)) {
				sql.append(" PRIMARY KEY");
				if (info.pkAuto) {
					sql.append(" AUTOINCREMENT");
				}
			}
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" )");
		return sql.toString();
	}

	/**
	 * 检查表是否存在
	 * 
	 * @param table
	 * @return
	 */
	public boolean checkTable(String table) {
		Cursor cursor = null;
		try {
			String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
					+ table + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					return true;
				}
			}

		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
			cursor = null;
		}
		return false;
	}

	/**
	 * 继承安卓splite数据库操作类
	 * 
	 * @author nanHuang
	 * 
	 */
	class SqliteDbHelper extends SQLiteOpenHelper {

		public SqliteDbHelper(Context context, String name, int version) {
			super(context, name, null, version);
		}

		@Override
		public void onCreate(SQLiteDatabase dbDatabase) {
			DbHelper.this.mDbHelpListener.onCreate();
		}

		@Override
		public void onUpgrade(SQLiteDatabase dbDatabase, int oldVersion,
				int newVersion) {
			DbHelper.this.isNeedUpgrade = true;
		}

	}

	/**
	 * @return the db
	 */
	public SQLiteDatabase getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

}
