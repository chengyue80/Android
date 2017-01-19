package com.yue.demo.net.weather;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.yue.demo.util.LogUtil;

public class WebServiceUtil {

	// 定义Web Service的命名空间
	static final String SERVICE_NS = "http://WebXml.com.cn/";
	// 定义Web Service提供服务的URL
	static final String SERVICE_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";

	/**
	 * 调用远程Web Service获取省份列表
	 * 
	 * @return
	 */
	public static List<String> getProvinceList() {

		// 调用的web service 提供的方法
		final String methodName = "getRegionProvince";
		// 创建HttpTransportSE传输对象
		final HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// 使用SOAP1.1协议创建Envelop对象
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// 实例化SoapObject对象
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		envelope.bodyOut = soapObject;
		// 设置与.Net提供的Web Service保持较好的兼容性
		envelope.dotNet = true;

		FutureTask<List<String>> task = new FutureTask<List<String>>(
				new Callable<List<String>>() {

					@Override
					public List<String> call() throws Exception {

						// 调用Web Service
						// SOAPAction: "http://WebXml.com.cn/getRegionProvince"
						ht.call(SERVICE_NS + methodName, envelope);

						LogUtil.i(MyWeather.tag,
								"province envelope.getResponse() ： \n"
										+ envelope.getResponse().toString());

						if (envelope.getResponse().toString() != null) {
							// 获取服务器响应返回的SOAP消息
							SoapObject result = (SoapObject) envelope.bodyIn;
							SoapObject detail = (SoapObject) result
									.getProperty(methodName + "Result");
							// 解析服务器响应的SOAP消息。

							return parseProvinceOrCity(detail);
						}
						return null;
					}
				});

		new Thread(task).start();

		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 根据省份获取城市列表
	 * 
	 * @param province
	 * @return
	 */
	public static List<String> getCityListByProvince(String province) {

		final String methodName = "getSupportCityString";

		final HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		// 添加一个请求参数
		soapObject.addProperty("theRegionCode", province);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;

		FutureTask<List<String>> task = new FutureTask<List<String>>(
				new Callable<List<String>>() {

					@Override
					public List<String> call() throws Exception {

						ht.call(SERVICE_NS + methodName, envelope);
						LogUtil.i(MyWeather.tag,
								"city envelope.getResponse() ： \n"
										+ envelope.getResponse().toString());
						SoapObject detail;
						if (envelope.getResponse().toString() != null) {
							SoapObject result = (SoapObject) envelope.bodyIn;
							detail = (SoapObject) result.getProperty(methodName
									+ "Result");
							// 解析服务器响应的SOAP消息。
							return parseProvinceOrCity(detail);
						}
						return null;
					}
				});

		new Thread(task).start();
		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解析服务器响应的SOAP消息。
	 * 
	 * @param detail
	 *            服务器返回的省份、城市消息SoapObject
	 * @return
	 */
	private static List<String> parseProvinceOrCity(SoapObject detail) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < detail.getPropertyCount(); i++) {
			// 解析出每个省份
			// Mlog.d(MyWeather.tag, detail.getProperty(i).toString());
			String name = detail.getProperty(i).toString().split(",")[0];
			// String code = detail.getProperty(i).toString().split(",")[1];
			// Mlog.d(MyWeather.tag, "省份 ：" + name + "  code : " + code);
			result.add(name);
		}
		return result;
	}

	/**
	 * 根据城市获取天气
	 * 
	 * @param cityName
	 * @return
	 */
	public static SoapObject getWeatherByCity(String cityName) {

		LogUtil.d(MyWeather.tag, "cityName : " + cityName);
		final String methodName = "getWeather";
		// final HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		// ht.debug = true;
		// final SoapSerializationEnvelope envelope = new
		// SoapSerializationEnvelope(
		// SoapEnvelope.VER11);
		//
		// SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		// // 添加一个请求参数
		// soapObject.addProperty("theCityCode", cityName);
		// soapObject.addProperty("theUserID", "");
		// envelope.setOutputSoapObject(soapObject);
		// envelope.bodyOut = soapObject;
		// envelope.dotNet = true;
		//
		// FutureTask<SoapObject> task = new FutureTask<SoapObject>(
		// new Callable<SoapObject>() {
		//
		// @Override
		// public SoapObject call() throws Exception {
		//
		// if (ht == null) {
		// Mlog.d("=====111============");
		// } else {
		//
		// Mlog.d("========1222=========");
		// }
		// ht.call(SERVICE_NS + methodName, envelope);
		// Mlog.i(MyWeather.tag,
		// "weather envelope.getResponse() ： \n"
		// + envelope.getResponse().toString());
		// if (envelope.getResponse().toString() != null) {
		// SoapObject result = (SoapObject) envelope.bodyIn;
		// SoapObject detail = (SoapObject) result
		// .getProperty(methodName + "Result");
		// // 解析服务器响应的SOAP消息。
		// Mlog.d(MyWeather.tag, "==="
		// + parseProvinceOrCity(detail));
		// return detail;
		// }
		// return null;
		// }
		// });

		// new Thread(task).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("-------------------");
				SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
				// 给调用参数赋值
				soapObject.addProperty("theCityCode", "120");
				soapObject.addProperty("theUserID", "");
				// 设置一些基本参数
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.bodyOut = soapObject;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(soapObject);
				HttpTransportSE httpTransportSE = new HttpTransportSE(
						SERVICE_URL);
				try {
					// 实际调用webservice的操作
					httpTransportSE.call(SERVICE_URL + methodName, envelope);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 获得调用的结果
				SoapObject object = (SoapObject) envelope.bodyIn;
				System.out.println(parseProvinceOrCity(object));
			}
		}).start();
		// try {
		// return task.get();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// }
		return null;

	}

	/**
	 * 本地天气图标
	 */
	private static final String WEATHER_LOCATION_URL = "assets://weather/";

	/**
	 * 根据天气状态返回图标
	 * 
	 * @param state
	 * @return
	 */
	public static String getWeatherIcon(String state) {
		if (state.contains("大雪")) {
			return WEATHER_LOCATION_URL + "weather_big_snow.png";
		}
		if (state.contains("中雪")) {
			return WEATHER_LOCATION_URL + "weather_middle_snow.png";
		}
		if (state.contains("雪") && state.contains("雨")) {
			return WEATHER_LOCATION_URL + "weather_sleet.png";
		}
		if (state.contains("雪")) {
			return WEATHER_LOCATION_URL + "weather_small_snow.png";
		}
		if (state.contains("雾") || state.contains("霾")) {
			return WEATHER_LOCATION_URL + "weather_fog.png";
		}
		if (state.contains("雷阵雨")) {
			return WEATHER_LOCATION_URL + "weather_thundershower.png";
		}
		if (state.contains("大雨") || state.contains("暴雨")) {
			return WEATHER_LOCATION_URL + "weather_big_rain.png";
		}
		if (state.contains("中雨")) {
			return WEATHER_LOCATION_URL + "weather_middle_rain.png";
		}
		if (state.contains("雨")) {
			return WEATHER_LOCATION_URL + "weather_small_rain.png";
		}
		if (state.contains("风")) {
			return WEATHER_LOCATION_URL + "weather_wind.png";
		}
		if (state.contains("多云")) {
			return WEATHER_LOCATION_URL + "weather_cloudy.png";
		}
		if (state.contains("晴")) {
			return WEATHER_LOCATION_URL + "weather_sun.png";
		}
		return WEATHER_LOCATION_URL + "weather_cloudy.png";
	}

}
