package com.yue.demo.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * 英语单词表
 * 
 * @author chengyue
 * 
 */
public class Dict extends Activity {

    protected final static String Tag = Dict.class.getSimpleName();
    private ListView listView = null;
    private MyDatabaseHelper dbHelper = null;
    protected String DB_NAME = "MyDict.db3";
    protected static final String TABLE_NAME = "dict";
    protected static final String KEY_WORD = "word";
    protected static final String KEY_DETIAL = "detail";

    private EditText et_title, et_content, et_key;
    private Button insert, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        dbHelper = new MyDatabaseHelper(this, DB_NAME, null, 1);

        insert.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String word = et_title.getText().toString();
                String detail = et_content.getText().toString();
                // 插入生词记录
                insertData(dbHelper.getReadableDatabase(), word, detail);
                Toast.makeText(Dict.this, "添加单词成功！", Toast.LENGTH_LONG).show();
            }
        });

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String key = et_key.getText().toString();
                ArrayList<Map<String, String>> datas = new ArrayList<Map<String, String>>();

                Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                        "select * from " + TABLE_NAME + " where " + KEY_WORD
                                + " like ? or " + KEY_DETIAL + " like ?",
                        new String[] { "%" + key + "%", "%" + key + "%" });

                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<String, String>();
                    // 去除查询记录中2、3列的值
                    map.put(KEY_WORD, cursor.getString(1));
                    map.put(KEY_DETIAL, cursor.getString(2));

                    // map.put(KEY_WORD,
                    // cursor.getString(cursor.getColumnIndex(KEY_WORD)));
                    // map.put(KEY_DETIAL,
                    // cursor.getString(cursor.getColumnIndex(KEY_DETIAL)));

                    datas.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(Dict.this, datas,
                        R.layout.listview_item, new String[] { KEY_WORD,
                                KEY_DETIAL }, new int[] {
                                R.id.listview_item_name,
                                R.id.listview_item_value });
                listView.setAdapter(adapter);
            }
        });

    }

    protected void insertData(SQLiteDatabase db, String word, String detail) {
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(NULL , ? , ?);",
                new String[] { word, detail });

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        et_title = new EditText(this);
        et_title.setLayoutParams(MainActivity.layoutParams_mw);
        et_title.setHint("word");
        ll.addView(et_title);

        et_content = new EditText(this);
        et_content.setLayoutParams(MainActivity.layoutParams_mw);
        et_content.setHint("detail");
        ll.addView(et_content);

        insert = new Button(this);
        insert.setLayoutParams(MainActivity.layoutParams_ww);
        insert.setText("insert");
        ll.addView(insert);

        et_key = new EditText(this);
        et_key.setLayoutParams(MainActivity.layoutParams_mw);
        et_key.setHint("请输入查询的单词信息");
        ll.addView(et_key);

        search = new Button(this);
        search.setLayoutParams(MainActivity.layoutParams_ww);
        search.setText("search");
        ll.addView(search);

        // 获取列出全部文件的ListView
        listView = new ListView(this);
        listView.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(listView);

        setContentView(ll);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
