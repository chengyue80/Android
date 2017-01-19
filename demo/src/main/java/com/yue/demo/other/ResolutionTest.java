package com.yue.demo.other;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ResolutionTest extends Activity {

    private final static String TAG = "Dip_pxTest";

    private float density;
    private int densityDPI;

    private TextView tv_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        tv_share = new TextView(this);
        tv_share.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        tv_share.setText("��ȡ����Ϣ");
        ll.addView(tv_share);

        setContentView(ll);

        getDefaultDisplayScreenSize();
        getDefaultDisplayScreenDensityDPI();
        getDisplayMetricsScreenSize();
        getDisplayMetricsScreenDensityDPI();

    }

    void getDefaultDisplayScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidthDip = dm.widthPixels;// ��Ļ�?dip���磺320dip��
        int screenHeightDip = dm.heightPixels;// ��Ļ�?dip���磺533dip��

        float density = dm.density;// ��Ļ�ܶȣ����ر���0.75/1.0/1.5/2.0��
        densityDPI = dm.densityDpi;// ��Ļ�ܶȣ�ÿ�����أ�120/160/240/320��
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        int w = dm.widthPixels;// ��Ļ�?px���磺480px��
        int h = dm.heightPixels;// ��Ļ�ߣ�px���磺800px��
        Log.d(TAG, "ScreenSize() " + "screenWidthDip=" + screenWidthDip
                + "; screenHeightDip=" + screenHeightDip);

        Log.d(TAG, "ScreenSize() " + "w=" + w + "; h=" + h);
    }

    void getDefaultDisplayScreenDensityDPI() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        density = dm.density;// ��Ļ�ܶȣ����ر���0.75/1.0/1.5/2.0��
        densityDPI = dm.densityDpi;// ��Ļ�ܶȣ�ÿ�����أ�120/160/240/320��
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        Log.d(TAG, "ScreenDensityDPI() " + "xdpi=" + xdpi + "; ydpi=" + ydpi);
        Log.d(TAG, "ScreenDensityDPI() " + "density=" + density
                + "; densityDPI=" + densityDPI);
    }

    void getDisplayMetricsScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        float density = dm.density;// ��Ļ�ܶȣ����ر���0.75/1.0/1.5/2.0��
        int densityDPI = dm.densityDpi;// ��Ļ�ܶȣ�ÿ�����أ�120/160/240/320��
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        Log.d(TAG, "MetricsScreenSize() " + "xdpi=" + xdpi + "; ydpi=" + ydpi);
        Log.d(TAG, "MetricsScreenSize() " + "density=" + density
                + "; densityDPI=" + densityDPI);

        int w = dm.widthPixels;// ��Ļ�?���أ��磺480px��
        int h = dm.heightPixels;// ��Ļ�ߣ����أ��磺800px��

        Log.d(TAG, "MetricsScreenSize() " + "w=" + w + "; h=" + h);
    }

    void getDisplayMetricsScreenDensityDPI() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        density = dm.density;// ��Ļ�ܶȣ����ر���0.75/1.0/1.5/2.0��
        densityDPI = dm.densityDpi;// ��Ļ�ܶȣ�ÿ�����أ�120/160/240/320��
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        Log.d(TAG, "MetricsScreenDensityDPI() " + "xdpi=" + xdpi + "; ydpi="
                + ydpi);
        Log.d(TAG, "MetricsScreenDensityDPI() " + "density=" + density
                + "; densityDPI=" + densityDPI);
    }

}
