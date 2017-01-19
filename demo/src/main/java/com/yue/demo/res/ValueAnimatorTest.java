package com.yue.demo.res;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yue.demo.R;

/**
 * 属性动画：不断渐变的背景色
 * 
 * @author chengyue
 * 
 */
public class ValueAnimatorTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.addView(new MyAnimtionView(this));
        setContentView(ll);
        // Button button = new Button(this);
        // button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));

    }

    class MyAnimtionView extends View {

        public MyAnimtionView(Context context) {
            super(context);
            // 加载动画资源
            ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater
                    .loadAnimator(getContext(), R.anim.color_anim);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            // 对该View本身应用属性动画
            objectAnimator.setTarget(this);
            // 开始指定动画
            objectAnimator.start();

        }

    }
}
