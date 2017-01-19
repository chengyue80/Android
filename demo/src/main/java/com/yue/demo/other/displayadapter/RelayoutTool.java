package com.yue.demo.other.displayadapter;

import java.lang.reflect.Field;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

/**
 * 
 * ���²��ֹ�����, ������ʽ����ͼ�㼶���������ţ�
 * 
 * @author {YueJinbiao}
 */
public class RelayoutTool {

    /**
     * ��ԭ��ͼ ��ߣ�padding��margin, ���ı������С ���������ţ����²��֣�
     * 
     * @param view
     *            ������ͼ������ͼ�㼶
     * @param scale
     *            ���ű���
     */
    public static void relayoutViewHierarchy(View view, float scale) {

        if (view == null) {
            return;
        }

        scaleView(view, scale);

        if (view instanceof ViewGroup) {
            View[] children = null;
            try {
                Field field = ViewGroup.class.getDeclaredField("mChildren");
                field.setAccessible(true);
                children = (View[]) field.get(view);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (children != null) {
                for (View child : children) {
                    relayoutViewHierarchy(child, scale);
                }
            }
        }
    }

    /**
     * ����ͼ���������ţ�������Ƕ����ͼ��
     * 
     * @param view
     *            ������Ƕ�ף����ŵ���View��
     * @param scale
     *            ���ű���
     */
    private static void scaleView(View view, float scale) {

        if (view instanceof TextView) {
            resetTextSize((TextView) view, scale);
        }

        int pLeft = convertFloatToInt(view.getPaddingLeft() * scale);
        int pTop = convertFloatToInt(view.getPaddingTop() * scale);
        int pRight = convertFloatToInt(view.getPaddingRight() * scale);
        int pBottom = convertFloatToInt(view.getPaddingBottom() * scale);
        view.setPadding(pLeft, pTop, pRight, pBottom);

        LayoutParams params = view.getLayoutParams();
        scaleLayoutParams(params, scale);

    }

    /**
     * ����ͼ�������԰��������ã�
     * 
     * @param params
     * @param scale
     *            ���ű���
     */
    public static void scaleLayoutParams(LayoutParams params, float scale) {
        if (params == null) {
            return;
        }
        if (params.width > 0) {
            params.width = convertFloatToInt(params.width * scale);
        }
        if (params.height > 0) {
            params.height = convertFloatToInt(params.height * scale);
        }

        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams mParams = (MarginLayoutParams) params;
            if (mParams.leftMargin > 0) {
                mParams.leftMargin = convertFloatToInt(mParams.leftMargin
                        * scale);
            }
            if (mParams.rightMargin > 0) {
                mParams.rightMargin = convertFloatToInt(mParams.rightMargin
                        * scale);
            }
            if (mParams.topMargin > 0) {
                mParams.topMargin = convertFloatToInt(mParams.topMargin * scale);
            }
            if (mParams.bottomMargin > 0) {
                mParams.bottomMargin = convertFloatToInt(mParams.bottomMargin
                        * scale);
            }
        }
    }

    /**
     * �� TextView���������ࣩ�ı���С���������ţ�
     * 
     * @param textView
     * @param scale
     *            ���ű���
     */
    private static void resetTextSize(TextView textView, float scale) {
        float size = textView.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * scale);
    }

    /**
     * float ת���� int С����������
     */
    private static int convertFloatToInt(float sourceNum) {
        return (int) (sourceNum + 0.5f);
    }

}
