package com.yue.demo.res;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yue.demo.R;

/**
 * 属性动画：布局动画<br/>
 * 主要使用LayoutTransition为布局的容器设置动画，<br/>
 * 当容器中的视图层次发生变化时存在过渡的动画效果。
 * 
 * 过渡的类型一共有四种：
 * 
 * LayoutTransition.APPEARING 当一个View在ViewGroup中出现时，对此View设置的动画
 * 
 * LayoutTransition.CHANGE_APPEARING
 * 当一个View在ViewGroup中出现时，此View对其他View位置造成影响，对其他View设置的动画
 * 
 * LayoutTransition.DISAPPEARING 当一个View在ViewGroup中消失时，对此View设置的动画
 * 
 * LayoutTransition.CHANGE_DISAPPEARING
 * 当一个View在ViewGroup中消失时，对此View对其他View位置造成影响，对其他View设置的动画
 * 
 * LayoutTransition.CHANGE 不是由于View出现或消失造成对其他View位置造成影响，然后对其他View设置的动画。
 * 
 * 注意动画到底设置在谁身上，此View还是其他View。
 * 
 * @author chengyue
 * 
 */
public class LayoutAnimations extends Activity implements
        OnCheckedChangeListener {

    private ViewGroup mViewGroup;
    private GridLayout mGridLayout;
    private int mVal;
    private LayoutTransition mTransition;

    private CheckBox mAppear, mChangeAppear, mDisAppear, mChangeDisAppear;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        setContentView(R.layout.layoutanim);
        mViewGroup = (ViewGroup) findViewById(R.id.id_anim_container);

        mAppear = (CheckBox) findViewById(R.id.id_anim_appear);
        mChangeAppear = (CheckBox) findViewById(R.id.id_anim_change_appear);
        mDisAppear = (CheckBox) findViewById(R.id.id_anim_disappear);
        mChangeDisAppear = (CheckBox) findViewById(R.id.id_anim_change_disappear);

        // 创建一个GridLayout
        mGridLayout = new GridLayout(this);
        // 设置每列5个按钮
        mGridLayout.setColumnCount(5);
        // 添加到布局中
        mViewGroup.addView(mGridLayout);
        // 默认动画全部开启
        mTransition = new LayoutTransition();
        mGridLayout.setLayoutTransition(mTransition);

        // 添加Button
        findViewById(R.id.id_anim_addBtns).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Button button = new Button(LayoutAnimations.this);
                        button.setText(++mVal + "");
                        mGridLayout.addView(button,
                                Math.min(1, mGridLayout.getChildCount()));
                        button.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mGridLayout.removeView(button);
                            }
                        });
                    }
                });
    }

    @SuppressLint("NewApi")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub

        mTransition = new LayoutTransition();
        mTransition.setAnimator(
                LayoutTransition.APPEARING,
                (mAppear.isChecked() ? mTransition
                        .getAnimator(LayoutTransition.APPEARING) : null));
        mTransition
                .setAnimator(
                        LayoutTransition.CHANGE_APPEARING,
                        (mChangeAppear.isChecked() ? mTransition
                                .getAnimator(LayoutTransition.CHANGE_APPEARING)
                                : null));
        mTransition.setAnimator(
                LayoutTransition.DISAPPEARING,
                (mDisAppear.isChecked() ? mTransition
                        .getAnimator(LayoutTransition.DISAPPEARING) : null));
        mTransition.setAnimator(
                LayoutTransition.CHANGE_DISAPPEARING,
                (mChangeDisAppear.isChecked() ? mTransition
                        .getAnimator(LayoutTransition.CHANGE_DISAPPEARING)
                        : null));
        mGridLayout.setLayoutTransition(mTransition);
    }

}
