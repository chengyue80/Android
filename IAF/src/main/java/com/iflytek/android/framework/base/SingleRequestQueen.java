package com.iflytek.android.framework.base;

import android.content.Context;
import com.iflytek.android.framework.volley.RequestQueue;
import com.iflytek.android.framework.volley.toolbox.HurlStack;
import com.iflytek.android.framework.volley.toolbox.Volley;

/**
 * 
 * @author chengyue
 * 
 */
public class SingleRequestQueen {
	private static volatile SingleRequestQueen sInstance;
	private Context mContext;
	private RequestQueue mRequestQueue;

	private SingleRequestQueen(Context context) {
		this.mContext = context;
	}

	public static SingleRequestQueen getInstance(Context context) {
		if (sInstance == null) {
			synchronized (SingleRequestQueen.class) {
				if (sInstance == null) {
					sInstance = new SingleRequestQueen(context);
				}
			}
		}
		return sInstance;
	}

	public RequestQueue getRequestQueue() {
		if (this.mRequestQueue == null) {
			this.mRequestQueue = Volley.newRequestQueue(
					this.mContext.getApplicationContext(), new HurlStack());
		}
		return this.mRequestQueue;
	}

	public void quitRequest(Context context) {
		if (this.mRequestQueue != null) {
			this.mRequestQueue.cancelAll(context.getClass().getSimpleName());
			this.mRequestQueue.stop();
			this.mRequestQueue = null;
		}
	}
}