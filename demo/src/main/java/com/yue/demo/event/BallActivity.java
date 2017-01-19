package com.yue.demo.event;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yue.demo.util.LogUtil;

/**
 * 轨迹球运动
 * 
 * @author chengyue
 * 
 */
public class BallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        root.setOrientation(LinearLayout.VERTICAL);

        Button button = new Button(this);
        button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        button.setText("回调事件传递");
        root.addView(button);
        DrawView drawView = new DrawView(this);
        drawView.setMinimumHeight(500);
        drawView.setMinimumWidth(300);
        root.addView(drawView);
        setContentView(root);

        button.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                LogUtil.d(MainActivity_Event.Tag + "keyCode : " + keyCode);
                // 只处理按下事件
                if (event.getKeyCode() == KeyEvent.ACTION_DOWN) {
                    LogUtil.d(MainActivity_Event.Tag
                            + "the keyDown in OnKeyListener");

                }

                // 返回false则继续向外传递事件
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.d(MainActivity_Event.Tag + "the keyDown in activity");
        finish();
        return true;
    }
}
