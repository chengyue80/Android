package com.yue.demo.ui.layout;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 霓虹灯效果
 * 
 * @author chengyue
 * 
 */
public class FrameLayout extends Activity {

    private int currColor = 0;

    private int[] colors = { R.color.color1, R.color.color2, R.color.color3,
            R.color.color4, R.color.color5, R.color.color6 };

    private int[] ids = { R.id.textView1, R.id.textView2, R.id.textView3,
            R.id.textView4, R.id.textView5, R.id.textView6 };
    TextView[] views = new TextView[ids.length];

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            if (msg.what == 0x123) {
                LogUtil.d("---color changed---");
                for (int i = 0; i < ids.length; i++) {
                    views[i].setBackgroundResource(colors[(i + currColor)
                            % ids.length]);
                }
                currColor++;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framelayouttest);

        for (int i = 0; i < ids.length; i++) {
            views[i] = (TextView) findViewById(ids[i]);
        }

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 200);
    }

}
