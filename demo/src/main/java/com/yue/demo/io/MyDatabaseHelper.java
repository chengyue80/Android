package com.yue.demo.io;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.yue.demo.util.LogUtil;

/**
 * SQLiteOpenHelper的子类，当程序通过子类连接数据库时<br/>
 * 若数据库不存在，则系统会自动调用onCreat创建数据库<br/>
 * 若程序创建该子类的实例时，传入的数据库版本号高于之前的版本号，<br/>
 * 系统会自动调用onUpgrade方法来更新数据库
 * 
 * @author chengyue
 * 
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    // "DROP TABLE IF EXISTS " + Dict.TABLE_NAME + ";\n"
    final String sql = "CREATE TABLE " + Dict.TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT , " + Dict.KEY_WORD
            + " , " + Dict.KEY_DETIAL + ");";

    public MyDatabaseHelper(Context context, String name,
            CursorFactory factory, int version) {
        super(context, name, factory, version);
        LogUtil.d(Dict.Tag, "----MyDatabaseHelper  init----");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.d(Dict.Tag, "----MyDatabaseHelper  onCreate----");
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.d(Dict.Tag, "----MyDatabaseHelper  onUpgrade----");
        LogUtil.i(Dict.Tag, "oldVersion : " + oldVersion + "  newVersion : "
                + newVersion);
    }

}
