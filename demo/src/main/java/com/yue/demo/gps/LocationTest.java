package com.yue.demo.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 根据定位数据
 * 
 * @author chengyue
 * 
 */
public class LocationTest extends Activity {

    private TextView textView;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 从GPS获取最近的定位信息
        Location location = lm
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateView(location);
        // 设置每3秒获取一次gps定位信息
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 8,
                new LocationListener() {

                    @Override
                    public void onStatusChanged(String provider, int status,
                            Bundle extras) {

                        LogUtil.d(MainActivity_GPS.gps,
                                "LocationTest onStatusChanged");

                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        LogUtil.d(MainActivity_GPS.gps,
                                "LocationTest onProviderEnabled");

                        // 当LocationProvider可用时，更新位置
                        updateView(lm.getLastKnownLocation(provider));
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        LogUtil.d(MainActivity_GPS.gps,
                                "LocationTest onProviderDisabled");
                        updateView(null);
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        LogUtil.d(MainActivity_GPS.gps,
                                "LocationTest onLocationChanged");
                        // 当gps定位信息改变时，更新位置
                        updateView(location);

                    }
                });

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

    // 更新EditText中显示的内容
    public void updateView(Location newLocation) {
        LogUtil.d(MainActivity_GPS.gps, "LocationTest updateView");

        if (newLocation != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("实时的位置信息：\n");
            sb.append("经度：");
            sb.append(newLocation.getLongitude());
            sb.append("\n纬度：");
            sb.append(newLocation.getLatitude());
            sb.append("\n高度：");
            sb.append(newLocation.getAltitude());
            sb.append("\n速度：");
            sb.append(newLocation.getSpeed());
            sb.append("\n方向：");
            sb.append(newLocation.getBearing());
            
            LogUtil.d(MainActivity_GPS.gps, sb.toString());
            textView.setText(sb.toString());
        } else {
            // 如果传入的Location对象为空则清空EditText
            textView.setText("");
        }
    }
}
