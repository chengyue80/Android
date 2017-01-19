package com.yue.demo.graphics;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * 逐帧动画
 * 
 * @author chengyue
 * 
 */
public class AnimationDrawableTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);

        Button play = new Button(this);
        play.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        play.setText("play");
        ll.addView(play);

        Button stop = new Button(this);
        stop.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        stop.setText("stop");
        ll.addView(stop);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(MainActivity.layoutParams_ww);
        imageView.setBackgroundResource(R.anim.animationdrawable);
        ll.addView(imageView);

        setContentView(ll);

        final AnimationDrawable anim = (AnimationDrawable) imageView
                .getBackground();

        play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                anim.start();
            }
        });

        stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                anim.stop();
            }
        });
    }
}
