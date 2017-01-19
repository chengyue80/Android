package com.yue.demo.ui.view;

import com.yue.demo.customview.TouchImageView;

import android.app.Activity;
import android.os.Bundle;

/**
 * android中手势操作图片的平移、缩放、旋转 
 * @author: chengyue
 * @createTime: 2015-8-19下午4:41:30
 * @version: v1.0
 */
public class TouchImageViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchImageView img = new TouchImageView(this);
        setContentView(img);
    }
}