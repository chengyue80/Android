package com.yue.demo.net.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yue.demo.R;

/**
 * 
 * @author Dave
 * 
 */
public class WeatherPic {
	public static Bitmap getPic(Context c, int index, int type) {
		Bitmap bmp = null;
		switch (index) {
		case 0:
			if (type == 0) {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			} else {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			}
			break;
		case 1:
			if (type == 0) {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			} else {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			}
			break;
		case 3:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 4:
		case 5:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 9:
		case 10:
		case 22:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 13:
			if (type == 0) {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			} else {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			}
			break;
		case 20:
		case 29:
		case 30:
		case 31:
			if (type == 0) {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			} else {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			}
			break;
		case 18:
		case 32:
			if (type == 0) {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			} else {
				bmp = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.small_no3_1);
			}
			break;
		case 19:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 16:
		case 17:
		case 27:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 26:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 2:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 7:
		case 8:
		case 21:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		/*
		 * bmp = BitmapFactory.decodeResource(c.getResources(),
		 * R.drawable.wip_sleet); break;
		 */
		case 14:
		case 15:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 6:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 11:
		case 12:
		case 23:
		case 24:
		case 25:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		case 28:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		default:
			bmp = BitmapFactory.decodeResource(c.getResources(),
					R.drawable.small_no3_1);
			break;
		}
		return bmp;
	}

	public static Bitmap getSmallPic(Context c, int index, int type) {
		int picSize = (int) c.getResources().getDimension(R.dimen.picSize);
		return Bitmap.createScaledBitmap(getPic(c, index, type), picSize,
				picSize, true);

	}
}
