package com.yue.demo.customview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.yue.demo.R;
import com.yue.demo.net.weather.WeatherActivity;
import com.yue.demo.net.weather.WeatherUtil;
import com.yue.demo.util.LogUtil;

/**
 * 天气趋势折线图
 * 
 * @author Dave
 * 
 */
public class TrendView extends View {

	private Paint mPointPaint; // 点 画笔
	private Paint mTextPaint; // 度数 画笔
	private Paint mLinePaint; // 上线 画笔
	private Paint mDashPaint; // 上线 画笔

	/** 天数 */
	private final int dayCount = 6;
	private int width[] = new int[dayCount]; // 横坐标 6个点的位置
	private float radius = 8; // 点 的半径
	/** 最高温度的集合 */
	private List<Integer> listTopTemp;
	private List<Integer> listLowTemp; // 最低温度的集合
	private List<String> listTimeWeak; // 周 的集合
	private List<String> listTimeDate; // 日期的集合
	private Bitmap[] topBitmaps; // 最高温度的图片
	private Context mContext;

	/** 日期文字 纵坐标 */
	private int height_txt;

	/** 天气图标 纵坐标 */
	private int height_weatherPic;

	/** 高温 纵坐标 */
	private int height_topTemp;
	/** 最高温度气温 */
	private int topTemp;

	/** 天气图标尺寸 */
	private int picSize;

	/**
	 * 每一度气温相隔的纵坐标如：2-3
	 */
	private int perTempSpace = 30;

	float space = 20f;// 气温的纵坐标

	public TrendView(Context context) {
		super(context);
		this.mContext = context;
		perTempSpace = (int) mContext.getResources().getDimension(
				R.dimen.weather_temp_perTempSpace);
		init();
	}

	public TrendView(Context context, AttributeSet attr) {
		super(context, attr);
		this.mContext = context;
		perTempSpace = (int) mContext.getResources().getDimension(
				R.dimen.weather_temp_perTempSpace);
		init();
	}

	/**
	 * 初始化变量
	 */
	private void init() {
		topBitmaps = new Bitmap[dayCount];
		// lowBmps = new Bitmap[dayCount];

		listTopTemp = new ArrayList<Integer>();
		listLowTemp = new ArrayList<Integer>();
		listTimeWeak = new ArrayList<String>();
		listTimeDate = new ArrayList<String>();
		// 初始化各个画笔
		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setColor(Color.WHITE);

		mLinePaint = new Paint();
		mLinePaint.setColor(Color.WHITE);
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(2);
		mLinePaint.setStyle(Style.STROKE);

		mDashPaint = new Paint();
		mDashPaint.setColor(Color.WHITE);
		mDashPaint.setAntiAlias(true);
		mDashPaint.setStrokeWidth(0.5f);
		mDashPaint.setStyle(Style.STROKE);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextSize(mContext.getResources().getDimension(
				R.dimen.weather_txt_size));
		mTextPaint.setTextAlign(Align.CENTER);

	}

	// 设置每个点的横坐标，等分(dayCount * 2)分，以及总的纵坐标
	public void setWidthHeight(int w, int h) {
		for (int i = 0; i < dayCount; i++) {
			width[i] = w * (2 * i + 1) / (dayCount * 2);

		}
		// width[0] = w / (dayCount * 2);
		// width[1] = w * 3 / (dayCount * 2);
		// width[2] = w * 5 / (dayCount * 2);
		// width[3] = w * 7 / (dayCount * 2);
		// width[4] = w * 9 / (dayCount * 2);
		// width[5] = w * 11 / (dayCount * 2);
	}

	// 设置温度
	public void setTemperature(List<Integer> top, List<Integer> low) {
		this.listTopTemp = top;
		this.listLowTemp = low;

		this.topTemp = listTopTemp.get(0);
	}

	// 设置时间
	public void setTime(List<String> timeWeak2, List<String> timeDate2) {
		this.listTimeWeak = timeWeak2;
		this.listTimeDate = timeDate2;

		postInvalidate();
	}

	// 设置图片
	public void setBitmap(List<String> topList) {
		for (int i = 0; i < dayCount; i++) {
			AssetManager assetManager = mContext.getAssets();
			Bitmap bitmap;
			try {
				String path = WeatherUtil.getWeatherIcon(topList.get(i))
						.replace("assets://", "");
				LogUtil.d(WeatherActivity.TAG, "path ； " + path);
				if (path.equals("")) {
					path = "weather/weather_cloudy.png";
				}
				bitmap = BitmapFactory.decodeStream(assetManager.open(path));
				LogUtil.d(WeatherActivity.TAG, "bitmap : " + bitmap.getWidth()
						+ "/" + bitmap.getHeight());
				topBitmaps[i] = Bitmap.createScaledBitmap(bitmap, picSize,
						picSize, true);

				// picSize = bitmap.getHeight() * 2;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		postInvalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		picSize = (int) mContext.getResources().getDimension(R.dimen.picSize);
		// 计算文字高度
		float fontHeight = fontMetrics.bottom - fontMetrics.top;
		LogUtil.d(WeatherActivity.TAG, "picSize : " + picSize);
		LogUtil.d(WeatherActivity.TAG, "fontHeight : " + fontHeight);
		LogUtil.d(WeatherActivity.TAG, "perTempSpace : " + perTempSpace);
		// BaseToast.showToastNotRepeat(mContext, " " + picSize, 1000);

		height_txt = (int) fontHeight;
		height_weatherPic = (int) (height_txt + picSize); // 图片的纵坐标（高温）
		LogUtil.d(WeatherActivity.TAG, "height_weatherPic : " + height_weatherPic);
		// 绘制日期和天气图片
		for (int i = 0; i < listTimeDate.size(); i++) {

			LogUtil.d(WeatherActivity.TAG, "date : " + listTimeDate.get(i));
			// 日期
			canvas.drawText(listTimeDate.get(i), width[i], height_txt,
					mTextPaint);
			// canvas.drawText(listTimeDate.get(i), width[i], height_txt
			// + fontHeight, mTextPaint);
			canvas.drawBitmap(topBitmaps[i],
					width[i] - topBitmaps[i].getWidth() / 2, height_weatherPic,
					null);

		}

		// 获取6天内高温的最高度
		for (int i = 0; i < listTopTemp.size(); i++) {
			if (topTemp < listTopTemp.get(i)) {
				this.topTemp = listTopTemp.get(i);
			}
		}
		LogUtil.d(WeatherActivity.TAG, "toptemp : " + topTemp);
		// 最高气温的纵坐标（高温）
		height_topTemp = (int) (height_weatherPic + picSize + fontHeight);
		LogUtil.d(WeatherActivity.TAG, "height_topTemp : " + height_topTemp);
		Path pathTemp = new Path();
		// 绘制高温
		for (int i = 0; i < listTopTemp.size(); i++) {

			// 每天高温的温度文字的height
			int tempTopHeight = height_topTemp + (topTemp - listTopTemp.get(i))
					* perTempSpace;
			// 每天高温点的height
			int dotTopHeight = (int) (tempTopHeight + space);

			// 绘制气温文字
			canvas.drawText(listTopTemp.get(i) + "°", width[i], tempTopHeight,
					mTextPaint);
			// 绘制气温的小点
			canvas.drawCircle(width[i], dotTopHeight, radius, mPointPaint);
			// 画6天的5条线气温连线

			if (i > 0) {// 从第2天画线
				// space1 = (listTopTemp.get(i + 1)) * perTempSpace;
				// 上一天的气温点
				int preTempHeight = (int) (height_topTemp
						+ (topTemp - listTopTemp.get(i - 1)) * perTempSpace + space);
				canvas.drawLine(width[i - 1], preTempHeight, width[i],
						dotTopHeight, mLinePaint);
			}

			// 每天低温点的height
			int dotLowHeight = (int) (height_topTemp
					+ (topTemp - listLowTemp.get(i)) * perTempSpace + fontHeight);

			// 绘制气温的小点
			canvas.drawCircle(width[i], dotLowHeight, radius, mPointPaint);
			// 绘制气温文字
			canvas.drawText(listLowTemp.get(i) + "°", width[i], dotLowHeight
					+ fontHeight, mTextPaint);
			// 画6天的5条线气温连线
			if (i > 0) {// 从第2天画线
				// space1 = (listTopTemp.get(i + 1)) * perTempSpace;
				// 上一天的气温点
				int preTempHeight = (int) (height_topTemp
						+ (topTemp - listLowTemp.get(i - 1)) * perTempSpace + fontHeight);
				canvas.drawLine(width[i - 1], preTempHeight, width[i],
						dotLowHeight, mLinePaint);
			}

			LogUtil.i(WeatherActivity.TAG,
					"temp : " + (dotTopHeight - dotLowHeight) + "  "
							+ (listTopTemp.get(i) - listLowTemp.get(i))
							* perTempSpace);

			PathEffect effect = new DashPathEffect(new float[] { 5, 5 }, 1);
			mDashPaint.setPathEffect(effect);
			Path path = new Path();
			path.moveTo(width[i], dotTopHeight);
			path.lineTo(width[i], dotLowHeight);
			canvas.drawPath(path, mDashPaint);

			if (i == 0) {
				pathTemp.moveTo(width[i], dotTopHeight);
			} else
				pathTemp.lineTo(width[i], dotTopHeight);
		}

		canvas.drawPath(pathTemp, mLinePaint);

	}

}
