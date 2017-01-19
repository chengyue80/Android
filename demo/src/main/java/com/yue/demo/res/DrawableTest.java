package com.yue.demo.res;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yue.demo.R;

/**
 * 
 * LayerDrawable的使用 <br/>
 * ShapeDrawableTest的使用
 * 
 * @author chengyue
 * 
 */
public class DrawableTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        Button layer = new Button(this);
        layer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        layer.setText("LayerDrawable");
        ll.addView(layer);

        Button shape = new Button(this);
        shape.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        shape.setText("ShapeDrawableTest");
        ll.addView(shape);

        Button chip = new Button(this);
        chip.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        chip.setText("ChipDrawableTest");
        ll.addView(chip);

        Button animation = new Button(this);
        animation.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        animation.setText("AnimationDrawable");
        ll.addView(animation);
        setContentView(ll);

        layer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(R.layout.res_seekbar);
            }
        });

        shape.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(R.layout.res_sharedrawable);
            }
        });

        chip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_book_twopane);
                LinearLayout ll = new LinearLayout(DrawableTest.this);
                ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                ImageView imageview = new ImageView(DrawableTest.this);
                imageview.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                imageview.setScaleType(ScaleType.FIT_START);
                imageview.setImageResource(R.drawable.my_clip);
                ll.addView(imageview);
                setContentView(ll);

                final ClipDrawable drawable = (ClipDrawable) imageview
                        .getDrawable();
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 如果该消息是本程序所发送的
                        if (msg.what == 0x1233) {
                            // 修改ClipDrawable的level值
                            drawable.setLevel(drawable.getLevel() + 200);
                        }
                    }
                };
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 0x1233;
                        // 发送消息，通知应用修改ClipDrawable对象的level值。
                        handler.sendMessage(msg);
                        // 取消定时器
                        if (drawable.getLevel() >= 10000) {
                            timer.cancel();
                        }
                    }
                }, 0, 300);
            }
        });

        animation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LinearLayout ll = new LinearLayout(DrawableTest.this);
                ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                final ImageView imageview = new ImageView(DrawableTest.this);
                imageview.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, 600));
                imageview.setScaleType(ScaleType.FIT_START);
                imageview.setImageResource(R.drawable.aaa);
                ll.addView(imageview);

                Button animation = new Button(DrawableTest.this);
                animation.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                animation.setText("开始动画");
                ll.addView(animation);
                setContentView(ll);
                animation.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Animation anim = AnimationUtils.loadAnimation(
                                DrawableTest.this, R.anim.my_anim);
                        // 设置动画结束后保留结束状态
                        // anim.setFillAfter(true);
                        // 设置动画结束后保留开始状态
                        // anim.setFillBefore(true);
                        anim.setRepeatCount(3);
                        // 重复的方式：从头开始
                        anim.setRepeatMode(Animation.RESTART);
                        imageview.startAnimation(anim);
                    }
                });
            }
        });

    }
}
