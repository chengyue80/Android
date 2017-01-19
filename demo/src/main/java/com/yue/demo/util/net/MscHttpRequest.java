package com.yue.demo.util.net;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;

import com.yue.demo.util.LogUtil;

public class MscHttpRequest extends Thread {

    public interface MscHttpRequestListener {
        public void onResult(MscHttpRequest request, int tag);

        public void onError(int errorCode);
    }

    private static final int MSG_RESULT = 0;
    private static final int MSG_ERROR = 1;

    private int mTimeOut = 20000; // 20s
    private MscHttpRequestListener mListener = null;
    private boolean mExit = false;
    private String mUrl = "";
    private byte[] mPostData = null;
    public static final int CONNECT_GET = 0;
    public static final int CONNECT_POST = 1;
    private int mConnectType = CONNECT_POST;
    private byte[] mResultData = null;

    private int mTag = 0;

    public MscHttpRequest() {

    }

    public MscHttpRequest(String url, String data) {
        setRequest(url, data);
    }

    public MscHttpRequest(String url, byte[] data) {
        setRequest(url, data);
    }

    public void setConectType(int type) {
        mConnectType = type;
    }

    /**
     * 设置请求参数
     * 
     * @param url
     *            路径
     * @param data
     *            ：post传入的参数
     */
    public void setRequest(String url, String data) {
        mUrl = url;
        try {
            mPostData = data.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setRequest(String url, byte[] data) {
        mUrl = url;
        mPostData = data;
    }

    public void setTimeOut(int timeout) {
        mTimeOut = timeout;
    }

    public void startRequest(MscHttpRequestListener listener) {
        startRequest(listener, 0);
    }

    public void startRequest(MscHttpRequestListener listener, int tag) {
        mListener = listener;
        mTag = tag;
        start();
    }

    public static URL composeBase64Url(String baseurl, String param)
            throws MalformedURLException, UnsupportedEncodingException {

        String url = baseurl;
        url += "?";
        url += Base64.encode(param.getBytes("utf-8"), Base64.DEFAULT);
        return (new URL(url));
    }

    public void cancel() {
        mExit = true;
    }

    @Override
    public void run() {
        if (mConnectType == CONNECT_POST)
            runPost(mTag);
        else
            runGet(mTag);
    }

    private void runGet(int tag) {
        // InputStream is = null;
        HttpURLConnection urlConn = null;
        BufferedInputStream bis = null;
        ByteArrayBuffer baf = null;
        try {
            LogUtil.d("Start connect server");
            URL url = new URL(mUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(mTimeOut);
            urlConn.setReadTimeout(mTimeOut);
            urlConn.setRequestMethod("GET");
            // urlConn.setRequestProperty("RANGE","bytes="+"");

            int responseCode = urlConn.getResponseCode();
            LogUtil.d("responseCode = " + responseCode);
            if (HttpURLConnection.HTTP_OK == responseCode) {
                bis = new BufferedInputStream(urlConn.getInputStream());
                baf = new ByteArrayBuffer(128);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                mResultData = baf.toByteArray();
                throwResult(tag);
            } else {
                LogUtil.d("MscHttpRequest connect error");
                throwError(MscMsg.ERROR_NET);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("MscHttpRequest error:" + e.toString() + " mTag = " + mTag);
            throwError(MscMsg.ERROR_NET);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                if (urlConn != null) {
                    urlConn.disconnect();
                    urlConn = null;
                }
            } catch (Exception e) {
            }
        }
    }

    private void throwResult(int tag) {
        if (mListener == null && !mExit)
            return;
        Message msg = mUiHandler.obtainMessage(MSG_RESULT, 0, tag, this);
        mUiHandler.sendMessage(msg);
    }

    private void throwError(int errorCode) {
        if (mListener == null && !mExit)
            return;
        Message msg = mUiHandler.obtainMessage(MSG_ERROR, errorCode, 0);
        mUiHandler.sendMessage(msg);
    }

    public void runPost(int tag) {

        // InputStream is = null;
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        ByteArrayBuffer baf = null;
        try {
            LogUtil.d("MscHttpRequest start Post");
            URL url = new URL(mUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");
            httpConn.setConnectTimeout(mTimeOut);
            httpConn.setReadTimeout(mTimeOut);
            httpConn.setRequestProperty("Content-length", "" + mPostData.length);
            // httpConn.setRequestProperty("Content-Type",
            // "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Charset", "utf-8");
            httpConn.setRequestProperty("Content-Type", "application/json");

            OutputStream outputStream = httpConn.getOutputStream();
            LogUtil.d("MscHttpRequest write length:" + mPostData.length);
            outputStream.write(mPostData);
            outputStream.flush();
            outputStream.close();

            int responseCode = httpConn.getResponseCode();
            LogUtil.d("responseCode = " + responseCode);
            if (HttpURLConnection.HTTP_OK == responseCode) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                mResultData = baf.toByteArray();
                throwResult(tag);
            } else {
                LogUtil.d("MscHttpRequest connect error");
                throwError(MscMsg.ERROR_NET);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throwError(MscMsg.ERROR_NET);
            LogUtil.d("MscHttpRequest error:" + e.toString());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                if (httpConn != null) {
                    httpConn.disconnect();
                    httpConn = null;
                }
            } catch (Exception e) {
            }
        }
    }

    public String getResultString() {
        String result = null;
        if (mResultData != null) {
            result = EncodingUtils.getString(mResultData, "utf-8");
            LogUtil.d("getResultString:" + result);
        }
        return result;
    }

    /**
     * 获取编码GB2312的数据
     * 
     * @return
     */
    public String getResultStringFromOther() {
        String result = null;
        if (mResultData != null) {
            result = EncodingUtils.getString(mResultData, "gb2312");
            LogUtil.d("getResultString:" + result);
        }
        return result;
    }

    public byte[] getResultBytes() {
        if (mResultData != null)
            return mResultData;
        else
            return null;
    }

    private Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.d("|" + mExit + "|" + mListener.toString());
            if (mExit || mListener == null)
                return;
            switch (msg.what) {
            case MSG_RESULT:
                mListener.onResult((MscHttpRequest) msg.obj, msg.arg2);
                break;
            case MSG_ERROR:
                mListener.onError(msg.arg1);
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }
    };
}
