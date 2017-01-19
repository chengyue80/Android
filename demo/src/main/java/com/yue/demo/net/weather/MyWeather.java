package com.yue.demo.net.weather;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 天气预报
 * 
 * @author chengyue
 * 
 */
public class MyWeather extends Activity {

	static final String tag = MyWeather.class.getSimpleName();
	private Spinner provinceSpinner;
	private Spinner citySpinner;
	private ImageView todayWhIcon1;
	private ImageView todayWhIcon2;
	private TextView textWeatherToday;
	private ImageView tomorrowWhIcon1;
	private ImageView tomorrowWhIcon2;
	private TextView textWeatherTomorrow;
	private ImageView afterdayWhIcon1;
	private ImageView afterdayWhIcon2;
	private TextView textWeatherAfterday;
	private TextView textWeatherCurrent;

	int mTimeOut = 20 * 1000;
	String mUrl = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";
	Handler mHandler = null;
	Handler updateUIHandler = null;

	List<String> weathers = new ArrayList<String>();
	
	
	/** */
	private List<Integer> maxlist;
	/** */
	private List<Integer> minlist;
	
	/** */
	private List<Integer> topPic;
//	private List<Integer> lowPic;
	
	/** */
	private List<String> timeWeak;
	/** */
	private List<String> timeDate;
	
	/**最高温度 */
	private int[] temTop = {12,12,6,9,12,6};
	/**最低温度 */
	private int[] temLow = {4,2,1,3,7,2};
	/**天气图片 */
	private int[] imgTop = {2,1,7,7,1,3};
//	private int[] imgLow = {1,2,2,7,3,7};
	/** 星期 */
	private String[] weak = {"星期一","星期二","星期三","星期四","星期五","星期六"};
	/** 日期  */
	private String[] date = {"4/1","4/2","4/3","4/4","4/5","4/6"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather1);
		todayWhIcon1 = (ImageView) findViewById(R.id.todayWhIcon1);
		todayWhIcon2 = (ImageView) findViewById(R.id.todayWhIcon2);
		textWeatherToday = (TextView) findViewById(R.id.weatherToday);
		tomorrowWhIcon1 = (ImageView) findViewById(R.id.tomorrowWhIcon1);
		tomorrowWhIcon2 = (ImageView) findViewById(R.id.tomorrowWhIcon2);
		textWeatherTomorrow = (TextView) findViewById(R.id.weatherTomorrow);
		afterdayWhIcon1 = (ImageView) findViewById(R.id.afterdayWhIcon1);
		afterdayWhIcon2 = (ImageView) findViewById(R.id.afterdayWhIcon2);
		textWeatherAfterday = (TextView) findViewById(R.id.weatherAfterday);
		textWeatherCurrent = (TextView) findViewById(R.id.weatherCurrent);

		updateUIHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				showWeather(weathers);

				View view = getLayoutInflater().inflate(
						android.R.layout.simple_list_item_1, null);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);
				textView.setFocusable(true);
				textView.setFocusableInTouchMode(true);
//				textView.setScrollContainer(true);
				textView.setMovementMethod(ScrollingMovementMethod
						.getInstance());
				textView.setText(msg.obj.toString());
				new AlertDialog.Builder(MyWeather.this).setView(view)
						.setPositiveButton("确定", null).show();
			}

		};

		// 获取程序界面中选择省份、城市的Spinner组件
		provinceSpinner = (Spinner) findViewById(R.id.province);
		citySpinner = (Spinner) findViewById(R.id.city);
		// 调用远程Web Service获取省份列表
		List<String> provinces = WebServiceUtil.getProvinceList();

		ListAdapter adapter = new ListAdapter(this, provinces);
		provinceSpinner.setAdapter(adapter);

		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				List<String> cities = WebServiceUtil
						.getCityListByProvince(provinceSpinner
								.getSelectedItem().toString());

				ListAdapter adapter = new ListAdapter(MyWeather.this, cities);
				citySpinner.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				// showWeather(citySpinner.getSelectedItem().toString());
				// WebServiceUtil.getWeatherByCity(citySpinner.getSelectedItem()
				// .toString());
				LogUtil.d(tag, "citySpinner cityName : "
						+ citySpinner.getSelectedItem().toString());
				Message msg = new Message();
				msg.what = 0x123;
				msg.obj = citySpinner.getSelectedItem().toString();
				mHandler.sendMessage(msg);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				mHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {

						super.handleMessage(msg);
						String cityName = (String) msg.obj;
						if (cityName.equals("") || cityName == null) {
							LogUtil.d(tag, "cityName is error");
							return;
						}
						request("theUserID=&theCityCode=" + cityName);
					}

				};
				Looper.loop();
			}
		}).start();

	}

	private void request(String mPostData) {
		LogUtil.d(tag, "run cityName : " + mPostData);
		HttpURLConnection conn = null;
		InputStream is = null;
		try {

			URL url = new URL(mUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(mTimeOut);
			conn.setReadTimeout(mTimeOut);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(mPostData.getBytes());
			outputStream.flush();
			outputStream.close();

			int requestCode = conn.getResponseCode();
			if (requestCode == 200) {
				LogUtil.d(tag, "网络请求成功 path : " + url.getPath());
				is = conn.getInputStream();

				DocumentBuilder builder = null;
				Document document = null;

				builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				document = builder.parse(is);

				// 找到根Element

				Element root = document.getDocumentElement();
				LogUtil.d(tag, "root element name : " + root.getNodeName());
				NodeList firstChildNode = root.getElementsByTagName("string");
				LogUtil.d(tag, "string length : " + firstChildNode.getLength());

				weathers.clear();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < firstChildNode.getLength(); i++) {

					Element element = (Element) firstChildNode.item(i);
					LogUtil.i(tag, element.getTextContent());
					sb.append(element.getTextContent());
					weathers.add(element.getTextContent());
				}
				Message message = new Message();
				message.what = 0x123;
				message.obj = sb.toString();
				updateUIHandler.sendMessage(message);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}
			} catch (Exception e) {
			}
		}
	}

	protected void showWeather(List<String> detail) {
		String weatherToday = null;
		String weatherTomorrow = null;
		String weatherAfterday = null;
		String weatherCurrent = null;
		int iconToday[] = new int[2];
		int iconTomorrow[] = new int[2];
		int iconAfterday[] = new int[2];
		// 获取远程Web Service返回的对象

		// 获取天气实况
		weatherCurrent = detail.get(4).toString();
		// 解析今天的天气情况
		String date = detail.get(7).toString();
		weatherToday = "今天：" + date.split(" ")[0];
		weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
		weatherToday = weatherToday + "\n气温：" + detail.get(8).toString();
		weatherToday = weatherToday + "\n风力：" + detail.get(9).toString() + "\n";
		iconToday[0] = parseIcon(detail.get(10).toString());
		iconToday[1] = parseIcon(detail.get(11).toString());
		// 解析明天的天气情况
		date = detail.get(12).toString();
		weatherTomorrow = "明天：" + date.split(" ")[0];
		weatherTomorrow = weatherTomorrow + "\n天气：" + date.split(" ")[1];
		weatherTomorrow = weatherTomorrow + "\n气温：" + detail.get(13).toString();
		weatherTomorrow = weatherTomorrow + "\n风力：" + detail.get(14).toString()
				+ "\n";
		iconTomorrow[0] = parseIcon(detail.get(15).toString());
		iconTomorrow[1] = parseIcon(detail.get(16).toString());
		// 解析后天的天气情况
		date = detail.get(17).toString();
		weatherAfterday = "后天：" + date.split(" ")[0];
		weatherAfterday = weatherAfterday + "\n天气：" + date.split(" ")[1];
		weatherAfterday = weatherAfterday + "\n气温：" + detail.get(18).toString();
		weatherAfterday = weatherAfterday + "\n风力：" + detail.get(19).toString()
				+ "\n";
		iconAfterday[0] = parseIcon(detail.get(20).toString());
		iconAfterday[1] = parseIcon(detail.get(21).toString());
		// 更新当天的天气实况
		textWeatherCurrent.setText(weatherCurrent);
		// 更新显示今天天气的图标和文本框
		textWeatherToday.setText(weatherToday);
		todayWhIcon1.setImageResource(iconToday[0]);
		todayWhIcon2.setImageResource(iconToday[1]);
		// 更新显示明天天气的图标和文本框
		textWeatherTomorrow.setText(weatherTomorrow);
		tomorrowWhIcon1.setImageResource(iconTomorrow[0]);
		tomorrowWhIcon2.setImageResource(iconTomorrow[1]);
		// 更新显示后天天气的图标和文本框
		textWeatherAfterday.setText(weatherAfterday);
		afterdayWhIcon1.setImageResource(iconAfterday[0]);
		afterdayWhIcon2.setImageResource(iconAfterday[1]);
	}

	// protected void showWeather(String city) {
	// String weatherToday = null;
	// String weatherTomorrow = null;
	// String weatherAfterday = null;
	// String weatherCurrent = null;
	// int iconToday[] = new int[2];
	// int iconTomorrow[] = new int[2];
	// int iconAfterday[] = new int[2];
	// // 获取远程Web Service返回的对象
	//
	// SoapObject detail = WebServiceUtil.getWeatherByCity(city);
	//
	// // 获取天气实况
	// weatherCurrent = detail.getProperty(4).toString();
	// // 解析今天的天气情况
	// String date = detail.getProperty(7).toString();
	// weatherToday = "今天：" + date.split(" ")[0];
	// weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
	// weatherToday = weatherToday + "\n气温："
	// + detail.getProperty(8).toString();
	// weatherToday = weatherToday + "\n风力："
	// + detail.getProperty(9).toString() + "\n";
	// iconToday[0] = parseIcon(detail.getProperty(10).toString());
	// iconToday[1] = parseIcon(detail.getProperty(11).toString());
	// // 解析明天的天气情况
	// date = detail.getProperty(12).toString();
	// weatherTomorrow = "明天：" + date.split(" ")[0];
	// weatherTomorrow = weatherTomorrow + "\n天气：" + date.split(" ")[1];
	// weatherTomorrow = weatherTomorrow + "\n气温："
	// + detail.getProperty(13).toString();
	// weatherTomorrow = weatherTomorrow + "\n风力："
	// + detail.getProperty(14).toString() + "\n";
	// iconTomorrow[0] = parseIcon(detail.getProperty(15).toString());
	// iconTomorrow[1] = parseIcon(detail.getProperty(16).toString());
	// // 解析后天的天气情况
	// date = detail.getProperty(17).toString();
	// weatherAfterday = "后天：" + date.split(" ")[0];
	// weatherAfterday = weatherAfterday + "\n天气：" + date.split(" ")[1];
	// weatherAfterday = weatherAfterday + "\n气温："
	// + detail.getProperty(18).toString();
	// weatherAfterday = weatherAfterday + "\n风力："
	// + detail.getProperty(19).toString() + "\n";
	// iconAfterday[0] = parseIcon(detail.getProperty(20).toString());
	// iconAfterday[1] = parseIcon(detail.getProperty(21).toString());
	// // 更新当天的天气实况
	// textWeatherCurrent.setText(weatherCurrent);
	// // 更新显示今天天气的图标和文本框
	// textWeatherToday.setText(weatherToday);
	// todayWhIcon1.setImageResource(iconToday[0]);
	// todayWhIcon2.setImageResource(iconToday[1]);
	// // 更新显示明天天气的图标和文本框
	// textWeatherTomorrow.setText(weatherTomorrow);
	// tomorrowWhIcon1.setImageResource(iconTomorrow[0]);
	// tomorrowWhIcon2.setImageResource(iconTomorrow[1]);
	// // 更新显示后天天气的图标和文本框
	// textWeatherAfterday.setText(weatherAfterday);
	// afterdayWhIcon1.setImageResource(iconAfterday[0]);
	// afterdayWhIcon2.setImageResource(iconAfterday[1]);
	// }

	/**
	 * 工具方法，该方法负责把返回的天气图标字符串，转换为程序的图片资源ID。
	 * 
	 * @param strIcon
	 * @return
	 */
	private int parseIcon(String strIcon) {
		if (strIcon == null)
			return -1;
		if ("0.gif".equals(strIcon))
			return R.drawable.a_0;
		if ("1.gif".equals(strIcon))
			return R.drawable.a_1;
		if ("2.gif".equals(strIcon))
			return R.drawable.a_2;
		if ("3.gif".equals(strIcon))
			return R.drawable.a_3;
		if ("4.gif".equals(strIcon))
			return R.drawable.a_4;
		if ("5.gif".equals(strIcon))
			return R.drawable.a_5;
		if ("6.gif".equals(strIcon))
			return R.drawable.a_6;
		if ("7.gif".equals(strIcon))
			return R.drawable.a_7;
		if ("8.gif".equals(strIcon))
			return R.drawable.a_8;
		if ("9.gif".equals(strIcon))
			return R.drawable.a_9;
		if ("10.gif".equals(strIcon))
			return R.drawable.a_10;
		if ("11.gif".equals(strIcon))
			return R.drawable.a_11;
		if ("12.gif".equals(strIcon))
			return R.drawable.a_12;
		if ("13.gif".equals(strIcon))
			return R.drawable.a_13;
		if ("14.gif".equals(strIcon))
			return R.drawable.a_14;
		if ("15.gif".equals(strIcon))
			return R.drawable.a_15;
		if ("16.gif".equals(strIcon))
			return R.drawable.a_16;
		if ("17.gif".equals(strIcon))
			return R.drawable.a_17;
		if ("18.gif".equals(strIcon))
			return R.drawable.a_18;
		if ("19.gif".equals(strIcon))
			return R.drawable.a_19;
		if ("20.gif".equals(strIcon))
			return R.drawable.a_20;
		if ("21.gif".equals(strIcon))
			return R.drawable.a_21;
		if ("22.gif".equals(strIcon))
			return R.drawable.a_22;
		if ("23.gif".equals(strIcon))
			return R.drawable.a_23;
		if ("24.gif".equals(strIcon))
			return R.drawable.a_24;
		if ("25.gif".equals(strIcon))
			return R.drawable.a_25;
		if ("26.gif".equals(strIcon))
			return R.drawable.a_26;
		if ("27.gif".equals(strIcon))
			return R.drawable.a_27;
		if ("28.gif".equals(strIcon))
			return R.drawable.a_28;
		if ("29.gif".equals(strIcon))
			return R.drawable.a_29;
		if ("30.gif".equals(strIcon))
			return R.drawable.a_30;
		if ("31.gif".equals(strIcon))
			return R.drawable.a_31;
		return 0;
	}

	class ListAdapter extends BaseAdapter {
		private Context context;
		private List<String> values;

		public ListAdapter(Context context, List<String> values) {
			this.context = context;
			this.values = values;
		}

		@Override
		public int getCount() {
			return values.size();
		}

		@Override
		public Object getItem(int position) {
			return values.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text = new TextView(context);
			text.setText(values.get(position));
			text.setTextSize(20);
			text.setTextColor(Color.BLACK);
			return text;
		}
	}

}
