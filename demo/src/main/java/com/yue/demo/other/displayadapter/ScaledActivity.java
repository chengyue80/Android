package com.yue.demo.other.displayadapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * 2013-8-16
 * 
 * @author {YueJinbiao}
 */
public class ScaledActivity extends Activity {

    // ���ű���
    private static float scale = 0;
    // ��׼UI��ƿ�ȣ�px��
    private static final float UI_DESIGN_WIDTH = 800.0f;

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        if (scale == 0) {
            initScreenScale();
        }
        RelayoutTool.relayoutViewHierarchy(view, scale);
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        if (scale == 0) {
            initScreenScale();
        }
        RelayoutTool.relayoutViewHierarchy(view, scale);
        RelayoutTool.scaleLayoutParams(params, scale);
        super.setContentView(view, params);
    }

    /**
     * ����Ļ�������ƿ�ȳ�ʼ�����ű���
     */
    private void initScreenScale() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        scale = displayMetrics.widthPixels / UI_DESIGN_WIDTH;
    }
}
