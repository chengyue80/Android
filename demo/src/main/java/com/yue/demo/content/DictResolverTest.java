package com.yue.demo.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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
import com.yue.demo.content.Words.Word;

public class DictResolverTest extends Activity {
    ContentResolver contentResolver;
    private ListView listView = null;
    private EditText et_title, et_content, et_key;
    private Button insert, search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        // 获取系统的ContentResolver对象
        contentResolver = getContentResolver();
        // 为insert按钮的单击事件绑定事件监听器
        insert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View source) {
                // 获取用户输入
                String word = et_title.getText().toString();
                String detail = et_content.getText().toString();
                // 插入生词记录
                ContentValues values = new ContentValues();
                values.put(Word.KEY_WORD, word);
                values.put(Words.Word.KEY_DETIAL, detail);
                Uri uri = contentResolver.insert(Words.Word.DICT_CONTENT_URI,
                        values);
                if (uri != null) {
                    // 显示提示信息
                    Toast.makeText(DictResolverTest.this, "添加生词成功！",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        // 为search按钮的单击事件绑定事件监听器
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String key = et_key.getText().toString();
                ArrayList<Map<String, String>> datas = new ArrayList<Map<String, String>>();

                Cursor cursor = contentResolver.query(
                        Words.Word.DICT_CONTENT_URI, null,
                        "word like ? or detail like ?", new String[] {
                                "%" + key + "%", "%" + key + "%" }, null);

                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<String, String>();
                    // 去除查询记录中2、3列的值
                    map.put(Word.KEY_WORD, cursor.getString(1));
                    map.put(Word.KEY_DETIAL, cursor.getString(2));

                    // map.put(KEY_WORD,
                    // cursor.getString(cursor.getColumnIndex(KEY_WORD)));
                    // map.put(KEY_DETIAL,
                    // cursor.getString(cursor.getColumnIndex(KEY_DETIAL)));

                    datas.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(
                        DictResolverTest.this, datas, R.layout.listview_item,
                        new String[] { Word.KEY_WORD, Word.KEY_DETIAL },
                        new int[] { R.id.listview_item_name,
                                R.id.listview_item_value });
                listView.setAdapter(adapter);
            }
        });
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

}