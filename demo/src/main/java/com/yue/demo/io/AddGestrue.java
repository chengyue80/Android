package com.yue.demo.io;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * @desc : 手势库载入增加
 * @author: chengyue
 * @createTime: 2015-9-17下午3:43:12
 * @version: v1.0
 */
public class AddGestrue extends Activity {

    // private static final String Tag = AddGestrue.class.getSimpleName();ss
    private GestureOverlayView gestureView = null;
    private String path = Environment.getExternalStorageDirectory().getPath()
            + "/test/mygestures";
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        textView = new TextView(this);
        textView.setLayoutParams(MainActivity.layoutParams_ww);
        textView.setText("请在下面屏幕上绘制手势");
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

        gestureView
                .addOnGesturePerformedListener(new OnGesturePerformedListener() {

                    @Override
                    public void onGesturePerformed(GestureOverlayView overlay,
                            final Gesture gesture) {
                        View save = getLayoutInflater().inflate(
                                R.layout.gestrue_save, null);
                        final EditText name = (EditText) save
                                .findViewById(R.id.gesture_name);
                        ImageView show = (ImageView) save
                                .findViewById(R.id.gestrue_show);
                        // 根据Gesture包含的手势创建一个位图
                        Bitmap bitmap = gesture.toBitmap(128, 128, 10,
                                0xffff0000);

                        show.setImageBitmap(bitmap);

                        Builder builder = new AlertDialog.Builder(
                                AddGestrue.this);
                        builder.setView(save);
                        builder.setPositiveButton("保存", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {

                                // 获取指定文件对应的手势库
                                GestureLibrary library = GestureLibraries
                                        .fromFile(path);
                                // 添加手势
                                library.addGesture(name.getText().toString(),
                                        gesture);
                                // 保存手势库
                                library.save();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                    }
                });

    }
}
