package com.yue.demo.ui.actionbar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * Description: <br/>
 * 结合Fragment使用ActionBar 实现Tab的导航
 * 
 * @author chengyue
 * @version 1.0
 */
@SuppressLint("NewApi")
public class ActionBar_TabNav extends Activity implements ActionBar.TabListener {
    private final String Tag = "actionbar >> ";
    private static final String SELECTED_ITEM = "selected_item";

    private final int CONTAINER_ID = 0x11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.setId(CONTAINER_ID);
        setContentView(ll);

        final ActionBar actionBar = getActionBar();
        // 设置ActionBar的导航方式：Tab导航
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // 依次添加3个Tab页，并为3个Tab标签添加事件监听器
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_launcher)
                .setTabListener(this));
        actionBar
                .addTab(actionBar.newTab().setText("第二页").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("第三页")
                .setIcon(R.drawable.ic_launcher).setTabListener(this));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 将当前选中的Fragment页的索引保存到Bundle中
        LogUtil.d(Tag + "onSaveInstanceState");
        outState.putInt(SELECTED_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        LogUtil.d(Tag + "onRestoreInstanceState");
        if (savedInstanceState.containsKey(SELECTED_ITEM)) {
            // 设置前面被保存的索引对应的Fragment页被选中
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(SELECTED_ITEM));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
        LogUtil.d(Tag + "onTabUnselected position : " + tab.getPosition());
    }

    // 当指定Tab被选中时激发该方法
    @Override
    public void onTabSelected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {

        LogUtil.d(Tag + "onTabSelected position : " + tab.getPosition());
        Toast.makeText(getApplicationContext(),
                "被选中的tab是 :" + tab.getPosition(), Toast.LENGTH_SHORT).show();
        // 创建一个新的Fragment对象
        Fragment fragment = new DummyFragment();
        // 创建一个Bundle对象，用于向Fragment传入参数
        Bundle args = new Bundle();
        args.putInt(DummyFragment.ARG_SECTION_NUMBER, tab.getPosition() + 1);
        // 向fragment传入参数
        fragment.setArguments(args);
        // 获取FragmentTransaction对象
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // 使用fragment代替该Activity中的container组件
        ft.replace(CONTAINER_ID, fragment);
        // 提交事务
        ft.commit();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
        LogUtil.d(Tag + "onTabReselected position : " + tab.getPosition());
        Toast.makeText(getApplicationContext(),
                "被onTabReselected 选中的tab是 :" + tab.getPosition(),
                Toast.LENGTH_SHORT).show();
    }
}
