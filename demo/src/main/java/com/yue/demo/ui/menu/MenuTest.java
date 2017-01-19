package com.yue.demo.ui.menu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.yue.demo.R;

/**
 * @author chengyue 点击屏幕的菜单按钮，出现子菜单，点击选择后弹出子菜单设置框
 * 
 */
public class MenuTest extends Activity {

    // 定义字体大小菜单项的标识
    final int FONT_10 = 0x111;
    final int FONT_12 = 0x112;
    final int FONT_14 = 0x113;
    final int FONT_16 = 0x114;
    final int FONT_18 = 0x115;
    // 定义普通菜单项的标识
    final int PLAIN_ITEM = 0x11b;
    // 定义字体颜色菜单项的标识
    final int FONT_RED = 0x116;
    final int FONT_BLUE = 0x117;
    final int FONT_GREEN = 0x118;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);

        editText = new EditText(this);
        editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        editText.setText("用于测试的内容");
        editText.setEnabled(false);
        ll.addView(editText);
        setContentView(ll);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // -------------向menu中添加字体大小的子菜单-------------
        SubMenu fontMenu = menu.addSubMenu(0, PLAIN_ITEM, 0, "设置字体大小");
        // 设置菜单的图标
        fontMenu.setIcon(R.drawable.font);
        // 设置菜单头的图标
        fontMenu.setHeaderIcon(R.drawable.tools);
        // 设置菜单头的标题
        fontMenu.setHeaderTitle("选择字体大小");
        fontMenu.add(0, FONT_10, 0, "10号字体");
        fontMenu.add(0, FONT_12, 0, "12号字体");
        fontMenu.add(1, FONT_14, 0, "14号字体");
        fontMenu.add(1, FONT_16, 0, "16号字体");
        fontMenu.add(1, FONT_18, 1, "18号字体");

        // -------------向menu中添加普通菜单项-------------
        menu.addSubMenu(1, 0x11c, 0, "普通菜单项");

        // -------------向menu中添加文字颜色的子菜单-------------
        SubMenu colorMenu = menu.addSubMenu("设置字体颜色");

        // 设置菜单头的图标
        colorMenu.setIcon(R.drawable.color);
        colorMenu.setHeaderIcon(R.drawable.color);
        // 设置菜单头的标题
        colorMenu.setHeaderTitle("选择字体颜色");
        colorMenu.add(0, FONT_RED, 0, "红色");
        colorMenu.add(0, FONT_GREEN, 0, "绿色");
        colorMenu.add(0, FONT_BLUE, 0, "蓝色");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
        case FONT_10:
            editText.setTextSize(10 * 2);
            break;
        case FONT_12:
            editText.setTextSize(12 * 2);
            break;
        case FONT_14:
            editText.setTextSize(14 * 2);
            break;
        case FONT_16:
            editText.setTextSize(16 * 2);
            break;
        case FONT_18:
            editText.setTextSize(18 * 2);
            break;
        case FONT_RED:
            editText.setTextColor(Color.RED);
            break;
        case FONT_GREEN:
            editText.setTextColor(Color.GREEN);
            break;
        case FONT_BLUE:
            editText.setTextColor(Color.BLUE);
            break;
        case PLAIN_ITEM:
            Toast toast = Toast.makeText(MenuTest.this, "您单击了普通菜单项",
                    Toast.LENGTH_SHORT);
            toast.show();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
