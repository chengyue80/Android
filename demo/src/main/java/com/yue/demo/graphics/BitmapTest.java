package com.yue.demo.graphics;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yue.demo.util.LogUtil;

/**
 * bitmap和bitmapfactory
 * 
 * @author chengyue
 * 
 */
public class BitmapTest extends Activity {
    private static final String TAG = BitmapTest.class.getSimpleName();
	String[] images = null;
    AssetManager assets = null;
    int currentImg = 0;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        Button bn = new Button(this);
        bn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        bn.setText("next");
        ll.addView(bn);

        final TextView textView = new TextView(this);
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        textView.setText("name:");
        // ll.addView(textView);

        image = new ImageView(this);
        image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        image.setScaleType(ScaleType.FIT_CENTER);
        ll.addView(image);
        setContentView(ll);
        try {
            assets = getAssets();
            // 获取/assets/目录下所有文件
            images = assets.list("");
            LogUtil.d(MainActivity_Graphics.Tag + "images.length : "
                    + images.length);
            for (String s : images) {
                LogUtil.i(TAG, MainActivity_Graphics.Tag + "path : " + s);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // 为bn按钮绑定事件监听器，该监听器将会查看下一张图片
        bn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View sources) {
                // 如果发生数组越界
                if (currentImg >= images.length) {
                    currentImg = 0;
                }
                // 找到下一个图片文件,如不是，则继续循环
                while (!images[currentImg].endsWith(".png")
                        && !images[currentImg].endsWith(".jpg")
                        && !images[currentImg].endsWith(".gif")) {
                    currentImg++;
                    // 如果已发生数组越界
                    if (currentImg >= images.length) {
                        currentImg = 0;
                    }

                }
                InputStream assetFile = null;
                try {
                    // 打开指定资源对应的输入流
                    assetFile = assets.open(images[currentImg++]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BitmapDrawable bitmapDrawable = (BitmapDrawable) image
                        .getDrawable();
                // 如果图片还未回收，先强制回收该图片
                if (bitmapDrawable != null
                        && !bitmapDrawable.getBitmap().isRecycled()) // ①
                {
                    bitmapDrawable.getBitmap().recycle();
                }
                // 改变ImageView显示的图片
                image.setImageBitmap(BitmapFactory.decodeStream(assetFile)); // ②
                // textView.setText(images[currentImg++]);
            }
        });
    }
}