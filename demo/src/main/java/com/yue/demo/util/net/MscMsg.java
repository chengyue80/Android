package com.yue.demo.util.net;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 服务端返回的消息对象
 * 
 * @author yjzhao
 * 
 */
public class MscMsg {
    private int mErrorCode = 0;
    private String mErrorText = null;
    public String mJsonData = null;

    public static final int ERROR_NET = -1;

    public MscMsg(String json) {
        mJsonData = json;
        JSONObject jsobject;
        try {
            jsobject = new JSONObject(new JSONTokener(mJsonData));
            mErrorCode = jsobject.getInt("ecode");
            mErrorText = "" + jsobject.getString("edesc");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorText() {
        return mErrorText;
    }

    public MscMsg(int errorcode) {
        mErrorCode = errorcode;
        mErrorText = "网络连接出错";
    }
}
