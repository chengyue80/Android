package com.yue.demo.ui.actionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Description: 下拉菜单导航
 * 
 * @author chengyue
 * @version 1.0
 */
public class ActionBar_DropDownNav extends Activity implements
        ActionBar.OnNavigationListener {
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
        // 设置ActionBar是否显示标题
        actionBar.setDisplayShowTitleEnabled(true);
        // 设置导航模式,使用List导航
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // 为actionBar安装ArrayAdapter
        actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(
                ActionBar_DropDownNav.this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                new String[] { "第一页", "第二页", "第三页" }), this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(SELECTED_ITEM)) {
            // 选中前面保存的索引对应的Fragment页
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(SELECTED_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 将当前选中的Fragment页的索引保存到Bundle中
        outState.putInt(SELECTED_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }

    // 当导航项被选中时激发该方法
    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // 创建一个新的Fragment对象
        Fragment fragment = new DummyFragment();
        // 创建一个Bundle对象，用于向Fragment传入参数
        Bundle args = new Bundle();
        args.putInt(DummyFragment.ARG_SECTION_NUMBER, position + 1);
        // 向fragment传入参数
        fragment.setArguments(args);
        // 获取FragmentTransaction对象
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // 使用fragment代替该Activity中的container组件
        ft.replace(CONTAINER_ID, fragment);
        // 提交事务
        ft.commit();
        return true;
    }
}
