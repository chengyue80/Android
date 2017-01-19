package com.yue.demo.ui.actionbar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * 普通的ActionBar，可以隐藏，显示actionbar 可通过应用图标启动主Activity 带选项菜单的actionbar
 * 
 * @author chengyue
 * 
 */
public class ActionBarTest extends Activity {
    private ActionBar actionBar;
    private TextView txt;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        Button show = new Button(this);
        show.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        show.setText("显示ActionBar");
        ll.addView(show);

        Button hide = new Button(this);
        hide.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        hide.setText("隐藏ActionBar");
        ll.addView(hide);

        txt = new TextView(this);
        txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        txt.setText("可通过应用程序图标返回主Activity");
        ll.addView(txt);

        setContentView(ll);

        // 获取该Activity的ActionBar，
        // 只有当应用主题没有关闭ActionBar时，该代码才能返回ActionBar
        actionBar = getActionBar();
        // 设置是否显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(true);
        // 将应用程序图标设置为可点击的按钮
        actionBar.setHomeButtonEnabled(true);
        // 将应用程序图标设置为可点击的按钮，并在图标上添加向左箭头
        // actionBar.setDisplayHomeAsUpEnabled(true);

        show.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                actionBar.show();
            }
        });

        hide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                actionBar.hide();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = new MenuInflater(this);
        // 状态R.menu.context对应的菜单，并添加到menu中
        inflator.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // 选项菜单的菜单项被单击后的回调方法
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (mi.isCheckable()) {
            mi.setChecked(true);
        }
        // 判断单击的是哪个菜单项，并针对性的作出响应。
        switch (mi.getItemId()) {
        case android.R.id.home:
            // 创建启动FirstActivity的Intent
            Intent intent = new Intent(this, MainActivity.class);
            // 添加额外的Flag，将Activity栈中处于FirstActivity之上的Activity弹出
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // 启动intent对应的Activity
            startActivity(intent);
            break;
        case R.id.font_10:
            txt.setTextSize(10 * 2);
            break;
        case R.id.font_12:
            txt.setTextSize(12 * 2);
            break;
        case R.id.font_14:
            txt.setTextSize(14 * 2);
            break;
        case R.id.font_16:
            txt.setTextSize(16 * 2);
            break;
        case R.id.font_18:
            txt.setTextSize(18 * 2);
            break;
        case R.id.red_font:
            txt.setTextColor(Color.RED);
            mi.setChecked(true);
            break;
        case R.id.green_font:
            txt.setTextColor(Color.GREEN);
            mi.setChecked(true);
            break;
        case R.id.blue_font:
            txt.setTextColor(Color.BLUE);
            mi.setChecked(true);
            break;
        case R.id.plain_item:
            Toast toast = Toast.makeText(ActionBarTest.this, "您单击了普通菜单项",
                    Toast.LENGTH_SHORT);
            toast.show();
            break;
        }
        return true;
    }

}