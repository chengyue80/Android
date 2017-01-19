package com.yue.demo.opengl;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * opengl 与3D应用开发
 * 
 * @author chengyue
 * 
 */
public class Mainactivity_Opengl extends LauncherActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        String[] mActivity = { "绘制2d图形" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mActivity);
        setListAdapter(adapter);
    }

    @Override
    protected Intent intentForPosition(int position) {
        switch (position) {
        case 0:
            return new Intent(this, Polygon.class);

        default:
            return super.intentForPosition(position);
        }
    }

}
