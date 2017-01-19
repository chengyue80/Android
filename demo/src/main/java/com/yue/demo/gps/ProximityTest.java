package com.yue.demo.gps;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.MainActivity;

/**
 * 临近警告
 * 
 * @author chengyue
 * 
 */
public class ProximityTest extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initView();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 定义目的地的大致经纬度
        double longitude = 113.39;
        double latitude = 23.13;
        // 定义半径/米
        float radius = 5000;
        Intent intent = new Intent();
        PendingIntent pi = PendingIntent.getBroadcast(this, -1, intent, 0);
        lm.addProximityAlert(latitude, longitude, radius, -1, pi);

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        textView = new TextView(this);
        textView.setLayoutParams(MainActivity.layoutParams_mw);
        textView.setText("系统支持的所有loactionProvider");
        ll.addView(textView);

        setContentView(ll);
    }

}
