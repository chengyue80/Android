package com.yue.demo.jsontest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yue.demo.util.LogUtil;

public class JsonTools {

	private static final String TAG = "JsonTestMsc";

	// 获取全部河流数据
	/**
	 * 参数fileName：为xml文档路径
	 */
	public static void getRiversFromXml(InputStream inputStream) {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		// 首先找到xml文件
		factory = DocumentBuilderFactory.newInstance();
		try {
			// 找到xml，并加载文档
			builder = factory.newDocumentBuilder();
			document = builder.parse(inputStream);
			// 找到根Element
			Element root = document.getDocumentElement();

			LogUtil.d(TAG, "root info  \ngetLocalName() :" + root.getLocalName()
					+ "\nroot.getNodeName() : " + root.getNodeName()
					+ "\nroot.getTagName() : " + root.getTagName());

			NodeList nodes = root.getElementsByTagName("content");
			LogUtil.d(TAG, "content length : " + nodes.getLength());
			Element content = (Element) nodes.item(0);
			NodeList channelNodes = content.getElementsByTagName("channel");
			LogUtil.d(TAG, "channel length : " + channelNodes.getLength());

			HashMap<String, String> mChannelIndex = new HashMap<String, String>();
			for (int i = 0; i < 55; i++) {
				mChannelIndex.put(String.valueOf(i), "-1");
			}
			LogUtil.d(TAG, "mChannelIndex size : " + mChannelIndex.size());
			// channel id="2" cid="2" name="CCTV-2财经" pid="cctv2" icon="2"
			// epg="2" cdnlive="1" replay="1" tn="18"
			// 遍历根节点所有子节点,content 下所有channel
			for (int i = 0; i < channelNodes.getLength(); i++) {
				// 获取channel 元素节点
				Element channelElement = (Element) (channelNodes.item(i));
				// 获取channel 中name属性值 频道名
				String name = channelElement.getAttribute("name");
				// 获取channel下 id 属性值 频道号
				String id = channelElement.getAttribute("id");// channelNumber
				String pid = channelElement.getAttribute("pid");
				String[] cid = channelElement.getAttribute("cid").split(",");// menuIndex
				// MLog.d(TAG, "id : " + id + "   cid : " + cid
				// + "   name : " + name + "   pid : " + pid);

				for (String menuId : cid) {
					if (mChannelIndex.containsKey(menuId)) {
						// 获取 channelIndex 并重新存储
						int index = Integer.parseInt(mChannelIndex.get(menuId));
						// Mlog.d(TAG, "name=" + name + "   menuIndex = " +
						// menuId
						// + "  channelIndxe = " + (index + 1));
						mChannelIndex.put(menuId, String.valueOf(++index));
					}
				}
				LogUtil.d(TAG,
						"name=" + name + "   cid : "
								+ channelElement.getAttribute("cid")
								+ "   menuIndex = " + cid[0]
								+ "  channelIndxe = "
								+ mChannelIndex.get(cid[0]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void getTvLive(String mUrl, String channel_data)
			throws JSONException {

		JSONArray channellist = new JSONArray();
		int num = 0;
		if (channel_data != null) {
			JSONArray jsonArray = null;
			if (mUrl.equals("http://api.ibestv.com/api/channel/list")) {

				// http://dsjapi.duapp.com/api/channel/list
				JSONTokener toker = new JSONTokener(channel_data);
				JSONObject obj = new JSONObject(toker);
				JSONArray actions = obj.getJSONArray("actions");
				jsonArray = new JSONArray(actions.getJSONObject(0)
						.getString("data").toString());
			} else {
				JSONTokener toker = new JSONTokener("{\"data\":" + channel_data
						+ "}");
				JSONObject obj = new JSONObject(toker);
				jsonArray = new JSONArray(obj.getString("data").toString());
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONArray channels = jsonArray.getJSONObject(i).getJSONArray(
						"channels");
				for (int j = 0; j < channels.length(); j++) {
					String name = channels.getJSONObject(j).getString("name");
					String url = channels.getJSONObject(j).getString("url");
					JSONObject channelItem = new JSONObject();

					channelItem.put("channelname", name);
					channelItem.put("cachehours", 0);
					channelItem.put("number", ++num);

					channellist.put(channelItem);

					// channelItem.name = name;
					// channelItem.cachehours = 0;
					// num++;
					// channelItem.number = String.valueOf(num);
				}
			}
			System.out.println(channellist.toString());
		} else {
			System.out.println("没有获取到数据！");
		}
	}

	/**
	 * 解析HDP直播的网络数据
	 * 
	 * @param msg
	 * @return
	 * @throws JSONException
	 */
	public static String getHDPJson(String msg) throws JSONException {
		JSONTokener toker = new JSONTokener(msg);
		JSONObject obj = new JSONObject(toker);
		JSONArray actions = obj.getJSONArray("live");
		actions.toString();
		for (int i = 0; i < actions.length(); i++) {
			String id = actions.getJSONObject(i).getString("id");
			String num = actions.getJSONObject(i).getString("num");
			String name = actions.getJSONObject(i).getString("name");
			String area = actions.getJSONObject(i).getString("area");
			int itemid = actions.getJSONObject(i).getInt("itemid");
			LogUtil.d("JsonTestMsc", "id:" + id + "  num:" + num + "  name:"
					+ name + "   area:" + area + "   itemid:" + itemid);

		}
		return actions.toString();
	}

	/**
	 * 解析HDP直播的网络数据
	 * 
	 * @param msg
	 * @return
	 * @throws JSONException
	 */
	public static String getFyzbJson(String msg) throws JSONException {
		JSONArray actions = new JSONArray(msg);
		LogUtil.d("JsonTestMsc",
				"getFyzbJson actions.length() : " + actions.length());
		for (int i = 0; i < actions.length(); i++) {
			String name = actions.getJSONObject(i).getString("name");
			String playerCode = actions.getJSONObject(i)
					.getString("playerCode");
			String type = actions.getJSONObject(i).getString("type");
			String sec_type = actions.getJSONObject(i).getString("sec-type");
			String status = actions.getJSONObject(i).getString("status");
			String imageUrl = actions.getJSONObject(i).getString("imageUrl");
			LogUtil.d("JsonTestMsc", "getFyzbJson name : " + name
					+ "  playerCode : " + playerCode);
		}
		// JSONTokener toker = new JSONTokener(msg);
		// JSONObject obj = new JSONObject(toker);
		// JSONArray actions = obj.getJSONArray("live");
		// actions.toString();
		// for (int i = 0; i < actions.length(); i++) {
		// String id = actions.getJSONObject(i).getString("id");
		// String num = actions.getJSONObject(i).getString("num");
		// String name = actions.getJSONObject(i).getString("name");
		// String area = actions.getJSONObject(i).getString("area");
		// int itemid = actions.getJSONObject(i).getInt("itemid");
		// MLog.d("JsonTestMsc", "id:" + id + "  num:" + num + "  name:" + name
		// + "   area:" + area + "   itemid:" + itemid);
		//
		// }
		return actions.toString();

		// {
		// "name": "北京体育",
		// "type": 1,
		// "sec-type": 101,
		// "playerCode": "btv6",
		// "status": "good",
		// "imageUrl":
		// "http://img9wx0.m1ppcdn.kukuplay.com/data//btv6_1408685352413/snapshot.jpg",
		// "program": "",
		// "online": 1376,
		// "otherSources": [
		// {
		// "name": "CNTV",
		// "icon": "",
		// "source":
		// "<div style=\"margin-left:-825px;margin-top:-540px;left:50%;top:50%;position:absolute;clip:rect(319px 1305px 859px 345px);z-index:100\"> <iframe src=\"http://tv.cntv.cn/live/btv6/?pt=chaoqing\" width=\"1650\" height=\"900\" border=\"0\" frameborder=\"no\" scrolling=\"no\"></iframe></div>",
		// "mobileSource": "http://tv.cntv.cn/live/btv6/?pt=chaoqing"
		// }
		// ],
		// "channelTypes": [
		// "1410841012605_150"
		// ]
		// }
	}

	/**
	 * 得到一个json类型的字符串对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getJsonString(String key, Object value) {
		JSONObject jsonObject = new JSONObject();
		// put和element都是往JSONObject对象中放入 key/value 对
		// jsonObject.put(key, value);
		return jsonObject.toString();
	}

	/**
	 * 得到一个json对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static JSONObject getJsonObject(String key, Object value) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 从指定的URL中获取数组
	 * 
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	public static String readParse(String urlPath) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream inStream = conn.getInputStream();
		while ((len = inStream.read(data)) != -1) {
			outStream.write(data, 0, len);
		}
		inStream.close();
		return new String(outStream.toByteArray());// 通过out.Stream.toByteArray获取到写的数据
	}

	/**
	 * 从 asset资源 中读取文件
	 * 
	 * @param channelList
	 * @return
	 */
	private String getJsonString(String channelList) {
		String str = "";
		StringBuilder sb = new StringBuilder();
		InputStreamReader inputStream = null;
		try {
			// inputStream = new
			// InputStreamReader(getResources().getAssets().open(channelList) );
			BufferedReader bf = new BufferedReader(inputStream);
			while ((str = bf.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
