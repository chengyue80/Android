package com.yue.demo.net.weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.android.framework.annotation.ViewInject;
import com.yue.demo.R;
import com.yue.demo.customview.TrendView;
import com.yue.demo.util.DateUtils;

public class WeatherActivity extends Activity implements Callback {
	public final static String TAG = "Weather";
	private List<Integer> listTopTemp;
	private List<Integer> listLowTemp;

	private List<String> listWeatherPic;
	// private List<Integer> lowPic;

	private List<String> listTimeWeeks;
	private List<String> listTimeDate;

	/**
	 * 锟斤拷锟斤拷
	 */
	private int[] temTop = { 30, 29, 24, 26, 27, 26, 26 };
	/**
	 * 锟斤拷锟斤拷
	 */
	private int[] temLow = { 17, 16, 17, 15, 17, 14, 16 };
	/**
	 * status
	 */
	private String[] status = { "锟斤拷锟斤拷", "锟斤拷锟斤拷", "锟斤拷锟斤拷", "锟斤拷", "锟斤拷", "锟斤拷锟斤拷", "锟斤拷锟斤拷" };
	// private int[] imgLow = {1,2,2,7,3,7};
	private String[] date = { "4/1", "4/2", "4/3", "4/4", "4/5", "4/6", "4/7" };

	/** 锟斤拷锟叫碉拷锟斤拷 */
	@ViewInject(id = R.id.weather_city, listenerName = "onClick", methodName = "btnClick")
	private TextView txt_city;

	/** 锟斤拷前锟铰讹拷 */
	@ViewInject(id = R.id.weather_temp, listenerName = "onClick", methodName = "btnClick")
	private TextView txt_currTemp;

	/** 锟斤拷锟斤拷图锟斤拷 */
	@ViewInject(id = R.id.weather_icon, listenerName = "onClick", methodName = "btnClick")
	private ImageView iv_cion;

	/** 锟斤拷锟斤拷锟斤拷锟斤拷 */
	@ViewInject(id = R.id.weather_status, listenerName = "onClick", methodName = "btnClick")
	private TextView txt_status;

	/** 锟斤拷锟斤拷锟斤拷锟斤拷 */
	@ViewInject(id = R.id.weather_AQI, listenerName = "onClick", methodName = "btnClick")
	private TextView txt_AQI;

	/** 锟斤拷汀锟斤拷锟斤拷锟铰讹拷 */
	@ViewInject(id = R.id.weather_topLowTemp, listenerName = "onClick", methodName = "btnClick")
	private TextView txt_toplowtemp;
	private Handler mHandler;
	private TrendView trend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather1);

		mHandler = new Handler(this);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 锟斤拷幕锟斤拷锟斤拷锟截ｏ拷锟界：480px锟斤拷
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		Log.d("WEATHER", "screenWidth/screenHeight : " + screenWidth + "/"
				+ screenHeight + "  " + (screenWidth / 720.0f));
		trend = (TrendView) findViewById(R.id.weather);
		trend.setWidthHeight((int) (screenWidth - 80 * (screenWidth / 720.0f)),
				trend.getHeight());

		listTopTemp = new ArrayList<Integer>();
		listLowTemp = new ArrayList<Integer>();
		listWeatherPic = new ArrayList<String>();
		// lowPic = new ArrayList<Integer>();
		listTimeWeeks = new ArrayList<String>();
		listTimeDate = new ArrayList<String>();

		for (int i = 0; i < temTop.length; i++) {
			listTopTemp.add(temTop[i]);
			listLowTemp.add(temLow[i]);
			listWeatherPic.add(status[i]);
			// lowPic.add(imgLow[i]);
			listTimeDate.add(date[i]);
		}
		
		trend.setTemperature(listTopTemp, listLowTemp);
		trend.setBitmap(listWeatherPic);
		trend.setTime(listTimeWeeks, getDateOfWeek(listTimeDate, 6));
		trend.postInvalidate();

	}

	
	/**
	 * 得到近七天的日期
	 * 
	 * @param dates
	 * @return
	 */
	public static List<String> getDateOfWeek(List<String> dates, int days) {
		dates.clear();
		dates.add("昨天");
		dates.add("今天");
		dates.add("明天");
		Date date = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd");
		for (int i = 3; i < days; i++) {
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + (i - 1));
			String time = format.format(calendar.getTime());
			System.out.println("time : " + time);
			dates.add(time);
		}
		return dates;
	}
	public void btnClick(View view) {
		switch (view.getId()) {

		default:
			break;
		}

	}

	public boolean handleMessage(Message msg) {
		return false;
	}

}
