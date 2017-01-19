package com.yue.demo.link.view;

import android.graphics.Bitmap;

/**
 * Description: 封装图片ID与图片本身的工具类
 * 
 * @author chengyue
 * @version 1.0
 */
public class PieceImage {
    // 用于在游戏界面上绘制方块
    private Bitmap image;
    // 资源ID代代表了Piece的标识
    private int imageId;

    // 有参数的构造器
    public PieceImage(Bitmap image, int imageId) {
        super();
        this.image = image;
        this.imageId = imageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
