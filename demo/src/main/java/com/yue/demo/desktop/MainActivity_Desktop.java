package com.yue.demo.desktop;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainActivity_Desktop extends LauncherActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        String[] mActivity = { "动态壁纸","桌面快捷方式" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mActivity);
        setListAdapter(adapter);

    }

    @Override
    protected Intent intentForPosition(int position) {

        switch (position) {
        case 0:
            startService(new Intent(this, LiveWallpaper.class));
            return new Intent(this, MainActivity_Desktop.class);
        case 1:
        	return new Intent(this, AddShortCut.class);

        default:
            return super.intentForPosition(position);
        }
    }
}
