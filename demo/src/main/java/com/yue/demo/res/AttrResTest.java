package com.yue.demo.res;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yue.demo.R;

/**
 * attribute 属性资源
 * 
 * @author chengyue
 * 
 */
public class AttrResTest extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        setContentView(R.layout.res_attribute);
    }
}