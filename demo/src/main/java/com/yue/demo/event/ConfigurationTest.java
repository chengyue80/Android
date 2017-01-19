package com.yue.demo.event;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 获取系统的设备状态
 * 
 * @author chengyue
 * 
 */
public class ConfigurationTest extends Activity {

    private TextView content;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.FILL_HORIZONTAL);
        ll.setOrientation(LinearLayout.VERTICAL);
        send = new Button(this);
        send.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        send.setText("获取手机信息");
        ll.addView(send);

        content = new TextView(this);
        content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        content.setText("系统设备信息 ");
        ll.addView(content);

        setContentView(ll);
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Configuration cfg = getResources().getConfiguration();
                String screen = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横向屏幕"
                        : "竖向屏幕";
                // 移动网络代号
                String mncCode = cfg.mnc + "";
                String naviName = cfg.orientation == Configuration.NAVIGATION_NONAV ? "没有方向控制"
                        : cfg.orientation == Configuration.NAVIGATION_WHEEL ? "滚轮控制方向"
                                : cfg.orientation == Configuration.NAVIGATION_DPAD ? "方向键控制方向"
                                        : "轨迹球控制方向";
                String touchName = cfg.touchscreen == Configuration.TOUCHSCREEN_NOTOUCH ? "无触摸屏"
                        : "支持触摸屏";
                content.setText("系统设备信息   " + "\n屏幕方向：" + screen + "\n移动网络代号:"
                        + mncCode + "\n手机方向控制设备:" + naviName + "\n触摸屏状态: "
                        + touchName);
            }
        });

    }
}
