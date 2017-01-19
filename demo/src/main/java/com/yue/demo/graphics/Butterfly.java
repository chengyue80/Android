package com.yue.demo.graphics;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 蝴蝶飞舞,逐帧动画与补间动画相结合
 * 
 * @author chengyue
 * 
 */
public class Butterfly extends Activity {

    private float curX = 0;
    private float curY = 30;

    // 记录蝴蝶imageview的下一个位置
    private float nextX = 0;
    private float nextY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ll.setBackgroundColor(Color.WHITE);

        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(MainActivity.layoutParams_ww);
        imageView.setBackgroundResource(R.anim.butterfly);
        ll.addView(imageView);

        setContentView(ll);

        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;

        final AnimationDrawable butterfly = (AnimationDrawable) imageView
                .getBackground();

        LogUtil.d(MainActivity_Graphics.Tag + "width : " + width);
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 0x123) {
                    // 横向一直向右飞
                    if (nextX > width) {
                        curX = nextX = 0;
                    } else {
                        nextX += 8;
                    }

                    if (nextY < 0) {
                        nextY = height - 10;
                    } else if (nextY > height) {
                        nextY = 10;
                    }
                    // 纵向上随机
                    nextY = curY + (float) (Math.random() * 10 - 5);
                    // 蝴蝶位移发生改变
                    TranslateAnimation animation = new TranslateAnimation(curX,
                            nextX, curY, nextY);
                    animation.setDuration(200);
                    curX = nextX;
                    curY = nextY;

                    imageView.startAnimation(animation);
                }
            }

        };

        butterfly.start();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 200);

    }
}
