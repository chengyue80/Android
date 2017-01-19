package com.yue.demo.io;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 手势识别
 * 
 * @author chengyue
 * 
 */
public class RecogniseGestrue extends Activity {

    private static final String Tag = RecogniseGestrue.class.getSimpleName();
    private GestureOverlayView gestureView = null;

    private TextView textView = null;

    private String path = Environment.getExternalStorageDirectory().getPath()
            + "/mygestures";
    // 记录手机上已有的手势库
    private GestureLibrary gestureLibrary = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        textView = new TextView(this);
        textView.setLayoutParams(MainActivity.layoutParams_ww);
        textView.setText("请在下面屏幕上绘制要识别的手势");
        ll.addView(textView);

        gestureView = new GestureOverlayView(this);
        gestureView.setLayoutParams(MainActivity.layoutParams_mm);
        gestureView
                .setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        gestureView.setGestureColor(Color.RED);
        gestureView.setGestureStrokeWidth(4);
        gestureView.setBackgroundColor(Color.GREEN);
        ll.addView(gestureView);
        setContentView(ll);

        gestureLibrary = GestureLibraries.fromFile(path);

        if (gestureLibrary.load()) {
            Toast.makeText(this, "手势文件装载成功！", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "手势文件装载失败！", Toast.LENGTH_SHORT).show();
        }

        gestureView
                .addOnGesturePerformedListener(new OnGesturePerformedListener() {

                    @Override
                    public void onGesturePerformed(GestureOverlayView overlay,
                            Gesture gesture) {
                        ArrayList<Prediction> predictions = gestureLibrary
                                .recognize(gesture);
                        ArrayList<String> result = new ArrayList<String>();
                        for (Prediction prediction : predictions) {

                            LogUtil.d(Tag, "prediction.name : " + prediction.name);
                            if (prediction.score > 2.0) {
                                result.add("与手势【" + prediction.name + "】相似度为 "
                                        + prediction.score);
                            }
                        }

                        if (result.size() > 0) {
                            ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                                    getApplicationContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    result.toArray());
                            new AlertDialog.Builder(RecogniseGestrue.this)
                                    .setTitle("识别结果：")
                                    .setAdapter(adapter, null)
                                    .setPositiveButton("确定", null).show();
                        } else {

                            Toast.makeText(RecogniseGestrue.this,
                                    "无法找到能匹配的手势！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
