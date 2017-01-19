package com.iflytek.android.framework.util.entity;

import java.util.HashMap;
import java.util.Map;

import com.iflytek.android.framework.util.HttpUtils;
import com.iflytek.android.framework.util.StringUtils;
import com.iflytek.android.framework.util.TimeUtils;

/**
 * HTTP响应类
 * 
 * @author wzgao
 *
 */
public class HttpResponse {

	// HTTP消息头
	private static final String EXPIRES = "expires";
	
	// HTTP消息头
	private static final String CACHE_CONTROL = "cache-control";

	// 连接地址
	private String url;
	
	// 响应体
	private String responseBody;
	
	// 响应消息头信息
	private Map<String, Object> responseHeaders;
	
	// 响应类型
	private int type;
	
	// 缓存的失效日期
	private long expiredTime;
	
	// 是否在客户端缓存
	private boolean isInCache;

	// 缓存时间
	private boolean isInitExpiredTime;

	// 响应码
	private int responseCode = -1;

	/**
	 * 初始化HttpResponse
	 * @param url 连接地址
	 */
	public HttpResponse(String url) {
		this.url = url;
		type = 0;
		isInCache = false;
		isInitExpiredTime = false;
		responseHeaders = new HashMap<String, Object>();
	}

	/**
	 * 初始化HttpResponse
	 */
	public HttpResponse() {
		responseHeaders = new HashMap<String, Object>();
	}

	/**
	 * 
	 * 描述：获取连接地址
	 * @return
	 * @throws
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * 描述：设置连接地址
	 * @param url 连接地址
	 * @throws
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * 描述：返回响应体
	 * @return
	 * @throws
	 */
	public String getResponseBody() {
		return responseBody;
	}

	/**
	 * 
	 * 描述：设置响应体
	 * @param responseBody
	 * @throws
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	/**
	 * 获取响应码
	 * 
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * 描述：设置响应码
	 * @param responseCode
	 * @throws
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * 
	 * 描述：设置响应头
	 * @param responseHeaders
	 * @throws
	 */
	public void setResponseHeaders(Map<String, Object> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	/**
	 * 获取响应类型
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设定响应类型
	 */
	public void setType(int type) {
		if (type < 0) {
			throw new IllegalArgumentException(
					"The type of HttpResponse cannot be smaller than 0.");
		}
		this.type = type;
	}

	/**
	 * 设定缓存的失效日期单位millis
	 * 
	 * @param expiredTime
	 */
	public void setExpiredTime(long expiredTime) {
		isInitExpiredTime = true;
		this.expiredTime = expiredTime;
	}

	/**
	 * 获取缓存的失效日期单位millis
	 */
	public long getExpiredTime() {
		if (isInitExpiredTime) {
			return expiredTime;
		} else {
			isInitExpiredTime = true;
			return expiredTime = getExpiresInMillis();
		}
	}

	/**
	 * 判断这个响应是否已过期
	 * 
	 * @return
	 */
	public boolean isExpired() {
		return TimeUtils.getCurrentTimeInLong() > expiredTime;
	}

	/**
	 * 是否缓存
	 * 
	 * @return the isInCache
	 */
	public boolean isInCache() {
		return isInCache;
	}

	/**
	 * 
	 * 描述：设定是否缓存
	 * @param isInCache
	 * @return
	 * @throws
	 */
	public HttpResponse setInCache(boolean isInCache) {
		this.isInCache = isInCache;
		return this;
	}

	/**
	 * 获取http请求头的值
	 * 
	 * @return 
	 */
	public String getExpiresHeader() {
		try {
			return responseHeaders == null ? null : (String) responseHeaders
					.get(EXPIRES);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取http请求头的值
	 * 
	 * @return 
	 */
	private int getCacheControlMaxAge() {
		try {
			String cacheControl = (String) responseHeaders.get(CACHE_CONTROL);
			if (!StringUtils.isEmpty(cacheControl)) {
				int start = cacheControl.indexOf("max-age=");
				if (start != -1) {
					int end = cacheControl.indexOf(",", start);
					String maxAge;
					if (end != -1) {
						maxAge = cacheControl.substring(
								start + "max-age=".length(), end);
					} else {
						maxAge = cacheControl.substring(start
								+ "max-age=".length());
					}
					return Integer.parseInt(maxAge);
				}
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 
	 * 描述：获取http请求头的值
	 * @return
	 * @throws
	 */
	private long getExpiresInMillis() {
		int maxAge = getCacheControlMaxAge();
		if (maxAge != -1) {
			return System.currentTimeMillis() + maxAge * 1000;
		} else {
			String expire = getExpiresHeader();
			if (!StringUtils.isEmpty(expire)) {
				return HttpUtils.parseGmtTime(getExpiresHeader());
			}
		}
		return -1;
	}

	/**
	 * set response header
	 * 
	 * @param field
	 * @param newValue
	 */
	public void setResponseHeader(String field, String newValue) {
		if (responseHeaders != null) {
			responseHeaders.put(field, newValue);
		}
	}
}
