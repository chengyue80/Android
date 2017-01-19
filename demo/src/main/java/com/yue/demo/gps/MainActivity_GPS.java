package com.yue.demo.gps;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * 
 * GPS 应用开发
 * 
 * @author chengyue
 * 
 */
public class MainActivity_GPS extends LauncherActivity {

    static final String gps = MainActivity_GPS.class.getSimpleName();

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        String[] mActivity = { "AllProvidersTest", "FreeProvidersTest",
                "LocationTest", "ProximityTest" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mActivity);
        setListAdapter(adapter);
    }

    @Override
    protected Intent intentForPosition(int position) {

        switch (position) {
        case 0:
            return new Intent(this, AllProvidersTest.class);

        case 1:
            return new Intent(this, FreeProvidersTest.class);

        case 2:
            return new Intent(this, LocationTest.class);

        case 3:
            return new Intent(this, ProximityTest.class);

        default:
            return super.intentForPosition(position);
        }
    }
}
