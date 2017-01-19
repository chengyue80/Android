package com.yue.demo.intent;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yue.demo.R;
import com.yue.demo.DemoInfo;
import com.yue.demo.DemoListAdapter;

public class MainActivity_Intent extends LauncherActivity {

    public static final String Tag = "intent >> ";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        ListView mListView = (ListView) findViewById(R.id.listView);
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter(this, demos));
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index,
                    long arg3) {
                onListItemClick(index);
            }
        });

    }

    private void onListItemClick(int index) {
        Intent intent = new Intent(this, demos[index].demoClass);
        startActivity(intent);
    }

    private DemoInfo[] demos = {
            new DemoInfo(R.string.demo_intent_name_DataTypeAttr,
                    R.string.demo_intent_title_DataTypeAttr, DataTypeAttr.class),

            new DemoInfo(R.string.demo_intent_name_tab,
                    R.string.demo_intent_title_tab, IntentTab.class) };
}
