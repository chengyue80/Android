package com.yue.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.NetWorkUtil;

/**
 * 获取网络和SIM卡信息
 * 
 * @author chengyue
 * 
 */
public class TelehonyStatus extends Activity {

	private ListView listView;
	// 声明代表状态名的数组
	private String[] statusNames;

	// 声明代表手机状态的集合
	private ArrayList<String> statusValues = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		// 获取系统的TelephonyManager对象
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 获取各种状态名称的数组
		statusNames = getResources().getStringArray(R.array.statusNames);
		// 获取代表SIM卡状态的数组
		String[] simState = getResources().getStringArray(R.array.simState);
		// 获取代表电话网络类型的数组
		String[] phoneType = getResources().getStringArray(R.array.phoneType);
		/**
		 * 返回当前移动终端的唯一标识
		 * 
		 * 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
		 */
		statusValues.add(tManager.getDeviceId());
		// //返回移动终端的软件版本，例如：GSM手机的IMEI/SV码。
		statusValues.add(tManager.getDeviceSoftwareVersion() != null ? tManager.getDeviceSoftwareVersion() : "未知");
		// 返回手机号码，对于GSM网络来说即MSISDN
		statusValues.add(tManager.getLine1Number());

		// 获取网络运营商代号 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
		statusValues.add(tManager.getNetworkOperator());
		// 获取网络运营商名称
		statusValues.add(tManager.getNetworkOperatorName());
		/**
        * 返回移动终端的类型
        * 
        * PHONE_TYPE_CDMA 手机制式为CDMA，电信
        * PHONE_TYPE_GSM 手机制式为GSM，移动和联通
        * PHONE_TYPE_NONE 手机制式未知
        */
		statusValues.add(phoneType[tManager.getPhoneType()]);
		// 获取设备所在位置
		statusValues.add(tManager.getCellLocation() != null ? tManager.getCellLocation().toString() : "未知位置");
		// 获取SIM卡的国别
		statusValues.add(tManager.getSimCountryIso());
		// 获取SIM卡序列号
		statusValues.add(tManager.getSimSerialNumber());
		// 获取SIM卡状态
		statusValues.add(simState[tManager.getSimState()]);

		/**
		 * 返回电话状态
		 * 
		 * CALL_STATE_IDLE 无任何状态时 CALL_STATE_OFFHOOK 接起电话时 CALL_STATE_RINGING
		 * 电话进来时
		 */
		statusValues.add(tManager.getCallState() + "");

		// 返回ISO标准的国家码，即国际长途区号
		statusValues.add(tManager.getNetworkCountryIso());

		/**
		 * 获取网络类型
		 * 
		 * NETWORK_TYPE_CDMA 网络类型为CDMA NETWORK_TYPE_EDGE 网络类型为EDGE
		 * NETWORK_TYPE_EVDO_0 网络类型为EVDO0 NETWORK_TYPE_EVDO_A 网络类型为EVDOA
		 * NETWORK_TYPE_GPRS 网络类型为GPRS NETWORK_TYPE_HSDPA 网络类型为HSDPA
		 * NETWORK_TYPE_HSPA 网络类型为HSPA NETWORK_TYPE_HSUPA 网络类型为HSUPA
		 * NETWORK_TYPE_UMTS 网络类型为UMTS
		 * 
		 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
		 */
		statusValues.add(NetWorkUtil.getNetworkClass(this) + "");

		// 获得ListView对象
		ArrayList<Map<String, String>> status = new ArrayList<Map<String, String>>();
		// 遍历statusValues集合，将statusNames、statusValues
		// 的数据封装到List<Map<String , String>>集合中
		for (int i = 0; i < statusValues.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map.put("name", statusNames[i]);
			map.put("value", statusValues.get(i));
			status.add(map);
		}
		// 使用SimpleAdapter封装List数据
		SimpleAdapter adapter = new SimpleAdapter(this, status, R.layout.listview_item,
				new String[] { "name", "value" }, new int[] { R.id.listview_item_name, R.id.listview_item_value });
		// 为ListView设置Adapter
		listView.setAdapter(adapter);
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		listView = new ListView(this);
		listView.setLayoutParams(MainActivity.layoutParams_mw);
		ll.addView(listView);

		setContentView(ll);
	}
}
