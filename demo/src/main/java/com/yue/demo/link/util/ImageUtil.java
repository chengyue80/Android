package com.yue.demo.link.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yue.demo.R;
import com.yue.demo.link.view.PieceImage;
import com.yue.demo.util.LogUtil;

/**
 * Description: 图片资源工具类, 主要用于读取游戏图片资源值
 * 
 * @author Chengyue
 * 
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";
    private static List<Integer> imageIDs = getImageValues();

    /** 保存所有连连看图片资源值(int类型) */
    public static List<Integer> getImageValues() {
        try {
            List<Integer> drawables = new ArrayList<Integer>();
            // 得到R.drawable所有的属性, 即获取drawable目录下的所有图片
            Field[] drawableFields = R.drawable.class.getFields();
            for (Field field : drawableFields) {
                if (field.getName().startsWith("p_")) {

                    LogUtil.i(TAG, "imageutil drawable name : " + field.getName());
                    drawables.add(field.getInt(R.drawable.class));
                }
            }
            return drawables;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 随机从sourceValues的集合中获取size个图片ID, 返回结果为图片ID的集合
     * 
     * @param sourceValues
     *            从中获取的集合
     * @param size
     *            需要获取的个数
     * @return size个图片ID的集合
     */
    public static List<Integer> getRandomValues(List<Integer> sourceValues,
            int size) {
        // 创建一个随机数生成器
        Random random = new Random();
        // 创建结果集合
        List<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < size; i++) {

            try {
                // 随机获取一个数字，大于、小于sourceValues.size()的数值
                int index = random.nextInt(sourceValues.size());
                // 从图片ID集合中获取该图片对象
                Integer image = sourceValues.get(index);
                // 添加到结果集中
                result.add(image);
            } catch (IndexOutOfBoundsException e) {
                return result;
            }
        }

        return result;

    }

    /**
     * 从drawable目录中中获取size个图片资源ID(以p_为前缀的资源名称), 其中size为游戏数量
     * 
     * @param size
     *            需要获取的图片ID的数量
     * @return size个图片ID的集合
     */

    public static List<Integer> getPlayValues(int size) {
        if (size % 2 != 0) {
            size += 1;
        }
        // 再从所有的图片值中随机获取size的一半数量
        List<Integer> playImageValues = getRandomValues(imageIDs, size / 2);
        // 将playImageValues集合的元素增加一倍（保证所有图片都有与之配对的图片）
        playImageValues.addAll(playImageValues);
        // 将所有图片ID随机“洗牌”
        Collections.shuffle(playImageValues);

        return playImageValues;

    }

    /**
     * 将图片ID集合转换PieceImage对象集合，PieceImage封装了图片ID与图片本身
     * 
     * @param context
     * @param resourceValues
     * @return size个PieceImage对象的集合
     */

    public static List<PieceImage> getPlayImage(Context context, int size) {
        List<PieceImage> result = new ArrayList<PieceImage>();
        // 获取图片ID组成的集合
        List<Integer> resourceValues = getPlayValues(size);
        LogUtil.d(TAG, "imageUtil getPlayImage resourceValues.size : "
                + resourceValues.size());
        for (Integer integer : resourceValues) {
            // 加载图片
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(), integer);
            if (bitmap == null)
                LogUtil.d(TAG, "imageUtil getPlayImage bitmap null...");

            PieceImage pieceImage = new PieceImage(bitmap, integer);
            result.add(pieceImage);
        }

        return result;
    }

    /**
     * 获取选中标识的图片
     * 
     * @param context
     * @return
     */
    public static Bitmap getSelectImage(Context context) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.selected);
        return bm;
    }
}
