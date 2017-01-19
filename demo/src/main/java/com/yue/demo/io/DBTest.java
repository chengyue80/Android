package com.yue.demo.io;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * SQLite 数据库
 * 
 * @author chengyue
 * 
 */
public class DBTest extends Activity {

    private SQLiteDatabase db = null;
    private ListView listView = null;
    private Button button = null;

    private final String DB_NAME = "test.db";
    private final String TABLE_NAME = "news_info";
    private final String KEY_TITLE = "title";
    private final String KEY_CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        final EditText et_title = new EditText(this);
        et_title.setLayoutParams(MainActivity.layoutParams_mw);
        et_title.setHint("输入title");
        ll.addView(et_title);

        final EditText et_content = new EditText(this);
        et_content.setLayoutParams(MainActivity.layoutParams_mw);
        et_content.setHint("输入content");
        ll.addView(et_content);

        button = new Button(this);
        button.setLayoutParams(MainActivity.layoutParams_ww);
        button.setText("insert");
        ll.addView(button);

        // 获取列出全部文件的ListView
        listView = new ListView(this);
        listView.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(listView);

        setContentView(ll);

        db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        db.beginTransaction();

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取用户输入
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();

                title = "title 1";
                content = "contnt 1";
                try {
                    // 执行插入语句
                    db.execSQL("insert into news_info values(null , ? , ?)",
                            new String[] { title, content });

                    // 填充SimpleCursorAdapter
                    Cursor cursor = db
                            .rawQuery("select * from news_info", null);
                    SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                            DBTest.this, R.layout.listview_item, cursor,
                            new String[] { KEY_TITLE, KEY_CONTENT }, new int[] {
                                    R.id.listview_item_name,
                                    R.id.listview_item_value },
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                    listView.setAdapter(cursorAdapter);
                } catch (Exception e) {
                    // 创建数据库表
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    db.execSQL("CREATE TABLE " + TABLE_NAME
                            + "(_id INTERGER PRIMARY KEY AUTOINCREMENT, "
                            + KEY_TITLE + " VARCHAR" + KEY_CONTENT + " VARCHAR");

                    // 执行插入语句
                    db.execSQL("INSERT INTO NEWS_INFO VALUES(NULL , ? , ?)",
                            new String[] { title, content });

                    // 填充SimpleCursorAdapter
                    Cursor cursor = db
                            .rawQuery("SELECT * FROM NEWS_INFO", null);
                    SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                            DBTest.this, R.layout.listview_item, cursor,
                            new String[] { KEY_TITLE, KEY_CONTENT }, new int[] {
                                    R.id.listview_item_name,
                                    R.id.listview_item_value },
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    listView.setAdapter(cursorAdapter);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
