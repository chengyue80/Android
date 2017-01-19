package com.yue.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yue.demo.R;
import com.yue.demo.DemoInfo;
import com.yue.demo.DemoListAdapter;

/**
 * 深入了解activity与fragment的入口
 * 
 * @author chengyue
 * 
 */
public class MainActivity_Activity extends Activity {
    public static String Tag = "Activity >> ";

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

    private static final DemoInfo[] demos = {
            new DemoInfo(R.string.demo_activity_name_expand,
                    R.string.demo_activity_name_expand,
                    ExpandableListActivityTest.class),

            new DemoInfo(R.string.demo_activity_name_preference,
                    R.string.demo_activity_name_preference,
                    PreferenceActivityTest.class),

            new DemoInfo(R.string.demo_activity_name_life,
                    R.string.demo_activity_name_life, Lifecycle.class),

            new DemoInfo(R.string.demo_activity_name_select,
                    R.string.demo_activity_name_select,
                    SelectBookActivity.class)

    };
}
