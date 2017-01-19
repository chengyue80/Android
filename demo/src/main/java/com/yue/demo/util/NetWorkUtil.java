package com.yue.demo.util;

import io.vov.vitamio.utils.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * 网咯连接的工具类
 * 
 * @author chengyue
 * 
 */
public class NetWorkUtil {
	private static final String TAG = NetWorkUtil.class.getSimpleName();

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		// 获取网络manager
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}
		NetworkInfo[] info = manager.getAllNetworkInfo();
		// 遍历所有可以连接的网络
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {

		// 获取网络manager
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}
		NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}

		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * @param context
	 * @return 获取当前的网络状态<br\>
	 *         -1:没有网络 1:WIFI网络 <br\>
	 *         移动 ： 2:cmnet 3:cmwap <br\>
	 *         电信 ： 4:ctnet 5:ctwap <br\>
	 *         联通 ： 6:uninet 7:uniwap
	 */
	public static int getAPNType(Context context) {
		// 网络状态，连接wifi，cmnet是直连互联网的，cmwap是需要代理，noneNet是无连接的
		// 一速度来说：wifi > cmnet >cmwap > noneNet
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return -1;
		}
		LogUtil.d(TAG, "netWorkInfo : " + networkInfo.toString());
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_WIFI) {
			// wifi
			return 1;
		} else if (nType == ConnectivityManager.TYPE_MOBILE) {
			String type = networkInfo.getExtraInfo();
			if (type.equalsIgnoreCase("cmnet")) {
				// cmnet
				return 2;
			} else if (type.equalsIgnoreCase("cmwap")) {
				// cmwap
				return 3;
			} else if (type.equalsIgnoreCase("ctnet")) {
				// ctnet
				return 4;
			} else if (type.equalsIgnoreCase("ctwap")) {
				// ctwap
				return 5;
			} else if (type.equalsIgnoreCase("uninet")) {
				// uninet
				return 6;
			} else if (type.equalsIgnoreCase("uniwap")) {
				// uniwap
				return 7;
			} else {
				return 2;
			}
		}
		return -1;

	}

	/**
	 * 修改Wifi状态
	 * 
	 * @param context
	 *            上下文
	 * @param status
	 *            true为开启Wifi，false为关闭Wifi
	 */
	public static void changeWIFIStatus(Context context, boolean status) {
		((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.setWifiEnabled(status);
	}

	public static void startNetSettingActivity(Context context) {
		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		context.startActivity(intent);
	}
	
	public static int getNetworkClass(Context context) {
		
		int type = 0;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			Method getNetworkClass = tm.getClass().getDeclaredMethod("getNetworkClass", int.class); 
			getNetworkClass.setAccessible(true);
			type = (Integer) getNetworkClass.invoke(tm, tm.getNetworkType());
			Log.d(TAG, "getNetworkClass type : " + type);
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return type;
    }
}