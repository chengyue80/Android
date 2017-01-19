package com.yue.demo.net.weather;

/**
 * 天气工具类
 * @author cllin
 *
 */
public class WeatherUtil {

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
