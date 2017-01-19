package com.yue.demo.ui.actionbar;

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
 * 
 * ActionBar的各种使用方法，注意theme的设置
 * 
 * @author chengyue
 * 
 */
public class MainActivity_ActionBar extends Activity {

    private DemoInfo[] demos = {

            new DemoInfo(R.string.demo_ui_actionbar_name_bar,
                    R.string.demo_ui_actionbar_title_bar, ActionBarTest.class),

            new DemoInfo(R.string.demo_ui_actionbar_name_tab,
                    R.string.demo_ui_actionbar_title_tab,
                    ActionBar_TabNav.class),

            new DemoInfo(R.string.demo_ui_actionbar_name_view,
                    R.string.demo_ui_actionbar_title_view, ActionViewTest.class),

            new DemoInfo(R.string.demo_ui_actionbar_name_tabswipebav,
                    R.string.demo_ui_actionbar_title_tabswipebav,
                    ActionBar_TabSwipeNav.class),

            new DemoInfo(R.string.demo_ui_actionbar_name_drop,
                    R.string.demo_ui_actionbar_title_drop,
                    ActionBar_DropDownNav.class)

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
