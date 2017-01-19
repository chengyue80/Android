package com.yue.demo.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.yue.demo.R;

/**
 * 设置data的值启动Activity，要修改Manifest中的Activity的data值
 * 
 * @author chengyue
 * @version 1.0
 */
public class DataTypeAttr extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_datatype);
    }

    // <data
    // android:host="www.fkjava.org"
    // android:mimeType="abc/xyz"
    // android:path="/mypath"
    // android:port="8888"
    // android:scheme="lee" />
    public void scheme(View source) {
        Intent intent = new Intent();
        // 只设置Intent的Data属性
        intent.setData(Uri.parse("lee://www.crazyit.org:1234/test"));
        startActivity(intent);
    }

    public void schemeHostPort(View source) {
        Intent intent = new Intent();
        // 只设置Intent的Data属性
        intent.setData(Uri.parse("lee://www.fkjava.org:8888/test"));
        startActivity(intent);
    }

    public void schemeHostPath(View source) {
        Intent intent = new Intent();
        // 只设置Intent的Data属性
        intent.setData(Uri.parse("lee://www.fkjava.org:1234/mypath"));
        startActivity(intent);
    }

    public void schemeHostPortPath(View source) {
        Intent intent = new Intent();
        // 只设置Intent的Data属性
        intent.setData(Uri.parse("lee://www.fkjava.org:8888/mypath"));
        startActivity(intent);
    }

    public void schemeHostPortPathType(View source) {
        Intent intent = new Intent();
        // 同时设置Intent的Data、Type属性
        intent.setDataAndType(Uri.parse("lee://www.fkjava.org:8888/mypath"),
                "abc/xyz");
        startActivity(intent);
    }
}
