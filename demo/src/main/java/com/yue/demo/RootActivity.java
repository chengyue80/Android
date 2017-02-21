package com.yue.demo;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.iflytek.android.framework.base.BaseActivity;

/**
 * @author : chengyue
 * @version : v1.0
 * @desc : BZFamily
 * @createTime : 17/2/21
 * @history : change on v1.0
 */
public class RootActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setNavigationBarTintEnabled(true);
//        tintManager.setStatusBarAlpha(0);
//        // 自定义颜色
////        tintManager.setTintColor(Color.parseColor("#24b7a4"));
    }

}
