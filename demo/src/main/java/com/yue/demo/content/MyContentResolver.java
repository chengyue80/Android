package com.yue.demo.content;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class MyContentResolver extends Activity {

    private static final String Tag = MyContentResolver.class.getSimpleName();
    private Button query, insert, update, delete;
    private ContentResolver resolver = null;
    private Uri uri = Uri.parse("content://org.yue.providers.myprovider/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initView();

        resolver = getContentResolver();

        query.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ContentResolver的query()方法。
                // 实际返回的是该Uri对应的ContentProvider的query()的返回值
                Cursor cursor = resolver.query(uri, null, "query_where", null,
                        null);
                LogUtil.d(Tag, "远程ContentProvide返回的Cursor为：" + cursor);
                Toast.makeText(MyContentResolver.this,
                        "远程ContentProvide返回的Cursor为：" + cursor,
                        Toast.LENGTH_SHORT).show();

            }
        });
        insert.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ContentResolver的insert()方法。
                // 实际返回的是该Uri对应的ContentProvider的insert()的返回值
                ContentValues values = new ContentValues();
                values.put("name", "MyContentResolver test");
                Uri newUri = resolver.insert(uri, values);
                LogUtil.d(Tag, "远程ContentProvide新插入记录的Uri为：" + newUri);
                Toast.makeText(MyContentResolver.this,
                        "远程ContentProvid新插入记录的Uri为：" + newUri,
                        Toast.LENGTH_SHORT).show();

            }
        });
        update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ContentResolver的update()方法。
                // 实际返回的是该Uri对应的ContentProvider的update()的返回值
                ContentValues values = new ContentValues();
                int count = resolver.update(uri, values, "update_where", null);
                LogUtil.d(Tag, "远程ContentProvide更新记录数为：" + count);
                Toast.makeText(MyContentResolver.this,
                        "远程ContentProvid更新记录数为：" + count, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ContentResolver的delete()方法。
                // 实际返回的是该Uri对应的ContentProvider的delete()的返回值

                int count = resolver.delete(uri, "delete_where", null);
                LogUtil.d(Tag, "远程ContentProvide删除记录数为：" + count);
                Toast.makeText(MyContentResolver.this,
                        "远程ContentProvid删除记录数为：" + count, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        query = new Button(this);
        query.setLayoutParams(MainActivity.layoutParams_mw);
        query.setText("query");
        ll.addView(query);

        insert = new Button(this);
        insert.setLayoutParams(MainActivity.layoutParams_mw);
        insert.setText("insert");
        ll.addView(insert);

        update = new Button(this);
        update.setLayoutParams(MainActivity.layoutParams_mw);
        update.setText("update");
        ll.addView(update);

        delete = new Button(this);
        delete.setLayoutParams(MainActivity.layoutParams_mw);
        delete.setText("delete");
        ll.addView(delete);

        setContentView(ll);
    }

}
