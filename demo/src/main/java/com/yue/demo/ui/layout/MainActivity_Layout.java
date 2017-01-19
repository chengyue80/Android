package com.yue.demo.ui.layout;

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

public class MainActivity_Layout extends Activity {

    private DemoInfo[] demos = {

            new DemoInfo(R.string.demo_ui_layout_name_table,
                    R.string.demo_ui_layout_title_table,
                    TableLayoutActivity.class),

            new DemoInfo(R.string.demo_ui_layout_name_frame,
                    R.string.demo_ui_layout_title_frame, FrameLayout.class),

            new DemoInfo(R.string.demo_ui_layout_name_grid,
                    R.string.demo_ui_layout_title_grid, GridLayoutTest.class)

    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        ListView mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new DemoListAdapter(this, demos));
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                onListItemClick(arg2);
            }
        });

    }

    private void onListItemClick(int index) {
        Intent intent = new Intent(this, demos[index].demoClass);
        startActivity(intent);
    }

}
