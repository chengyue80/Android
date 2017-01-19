package com.yue.demo.graphics;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * 补间动画
 * 
 * @author chengyue
 * 
 */
public class TweenAnimTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);

        Button play = new Button(this);
        play.setLayoutParams(MainActivity.layoutParams_ww);
        play.setText("play");
        ll.addView(play);

        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(MainActivity.layoutParams_mw);
        imageView.setBackgroundResource(R.drawable.flower);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        ll.addView(imageView);

        setContentView(ll);

        final Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.tween_anim);
        // 设置动画结束后保留结束状态
        anim.setFillAfter(true);
        // 加载第二份动画资源
        final Animation reverse = AnimationUtils.loadAnimation(this,
                R.anim.tween_anim_reverse);
        final Animation down = AnimationUtils.loadAnimation(this,
        		R.anim.art_down);
        // 设置动画结束后保留结束状态
        reverse.setFillAfter(true);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    imageView.startAnimation(reverse);
                }
            }
        };
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imageView.startAnimation(reverse);
                // 设置3.5秒后启动第二个动画
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        handler.sendEmptyMessage(0x123);
//                    }
//                }, 3500);
            }
        });
    }
}