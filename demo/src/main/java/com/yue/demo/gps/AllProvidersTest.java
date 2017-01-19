package com.yue.demo.gps;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.MainActivity;

public class AllProvidersTest extends Activity {

    private ListView listView;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final List<String> list = locationManager.getAllProviders();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                String name = list.get(position);
                // 通过名称获取指定的LocationProvider LocationManager.GPS_PROVIDER
                LocationProvider gps = locationManager.getProvider(name);
                Toast.makeText(AllProvidersTest.this,
                        "通过名称获取指定的LocationProvider：\n" + gps.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setLayoutParams(MainActivity.layoutParams_mw);
        textView.setText("系统支持的所有loactionProvider");
        ll.addView(textView);

        listView = new ListView(this);
        listView.setLayoutParams(MainActivity.layoutParams_mw);
        ll.addView(listView);

        setContentView(ll);
    }
}
