package com.yue.demo.ui.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomButton;

import com.yue.demo.R;
import com.yue.demo.RootActivity;

public class ImageTestActivity extends RootActivity {

    private ImageView img, smallImage;
    private Bitmap bitmap;
    private Button alphaPlus, alphaDel;
    private ZoomButton zoomIn, zoomOut;
    private int alpha = 255;
    private float towidth = 1;
    private float toheight = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_imagetest);

        img = (ImageView) findViewById(R.id.controller_imageTest);
        smallImage = (ImageView) findViewById(R.id.controller_smallimage);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qiao);
        img.setImageBitmap(bitmap);
        alphaPlus = (Button) findViewById(R.id.controller_btnAlpahPlus);
        alphaPlus.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                alpha = alpha - 20;
                if (alpha < 0) {
                    alpha = 0;
                    Toast.makeText(getBaseContext(), "无法再增加了！",
                            Toast.LENGTH_SHORT).show();
                }
                img.setAlpha(alpha);

            }
        });

        alphaDel = (Button) findViewById(R.id.controller_btnAlpahdel);
        alphaDel.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                alpha = alpha + 20;
                if (alpha > 255) {
                    alpha = 255;
                    Toast.makeText(getBaseContext(), "无法再减少了！",
                            Toast.LENGTH_SHORT).show();
                }
                img.setAlpha(alpha);

            }
        });
        // 图片原始的长和宽
        final int bwidth = bitmap.getWidth();
        final int bheight = bitmap.getHeight();
        // 放大
        zoomIn = (ZoomButton) findViewById(R.id.controller_btnZoomin);
        zoomIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 记录现在放大到什么级别了
                towidth = (float) (towidth * 1.25);
                toheight = (float) (toheight * 1.25);
                // 矩阵缩放变化
                Matrix scalMatrix = new Matrix();
                scalMatrix.postScale(towidth, toheight);
                // 构建新的bitmap
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bwidth,
                        bheight, scalMatrix, true);

                img.setImageBitmap(newBitmap);
            }
        });

        // 缩小
        zoomOut = (ZoomButton) findViewById(R.id.controller_btnZoomout);
        zoomOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 记录现在缩小到什么级别了

                towidth = (float) (towidth * 0.8);
                toheight = (float) (toheight * 0.8);
                // 矩阵缩放变化
                Matrix scalMatrix = new Matrix();
                scalMatrix.postScale(towidth, toheight);
                // 构建新的bitmap
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bwidth,
                        bheight, scalMatrix, true);

                img.setImageBitmap(newBitmap);
            }
        });

        img.setOnTouchListener(new OnTouchListener() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable) img
                        .getDrawable();
                // 获取第一个图片显示框中的位图
                Bitmap bitmap = bitmapDrawable.getBitmap();
                // bitmap图片实际大小与第一个ImageView的缩放比例
                double scale = bitmap.getWidth() / 320.0;
                // 获取需要显示的图片的开始点
                int x = (int) (event.getX() * scale);
                int y = (int) (event.getY() * scale);
                if (x + 120 > bitmap.getWidth()) {
                    x = bitmap.getWidth() - 120;
                }
                if (y + 120 > bitmap.getHeight()) {
                    y = bitmap.getHeight() - 120;
                }
                // 显示图片的指定区域
                smallImage.setImageBitmap(Bitmap.createBitmap(bitmap, x, y,
                        120, 120));
                smallImage.setAlpha(alpha);
                return false;
            }
        });
    }
}
