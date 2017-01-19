package com.yue.demo.jsontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;
import com.yue.demo.util.net.MscHttpRequest;
import com.yue.demo.util.net.MscHttpRequest.MscHttpRequestListener;

public class JsonTestMsc extends Activity {

    private String TAG = "JsonTestMsc";
    // public final String URL_PATH =
    // "http://mini.fengyunzhibo.com/channellist?img=1";//风云直播
    // public final String URL_PATH =
    // "http://api.hdpfans.com/apk_api/itv_json_v3.php";//HDP直播
    // public final String URL_PATH =
    // "http://dsjapi.duapp.com/api/channel/list";//电视家
    // public final String URL_PATH =
    // "http://yaokong.wukongtv.com/tvlive.json";//电视家
//    public final String URL_PATH = "http://api.ibestv.com/api/channel/list";// 电视家
     public final String URL_PATH =
     "http://l.iptv139.com:81/api/sunchip/tvlist.xml?type=list";// 橙子TV
    // public final String URL_PATH =
    // "http://live.booslink.net/api_zhibo/zhibo_channels.txt";
    protected MscHttpRequestListener mRequestBaseListener = new MscHttpRequestListener() {

        @Override
        public void onError(int errorCode) {
            Log.i(TAG, "mRequestBaseListener onError");
        }

        @Override
        public void onResult(MscHttpRequest request, int tag) {
            try {
                processData(request.getResultString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private int mTimeOut = 20000; // 20s
    public static final int CONNECT_GET = 0;
    public static final int CONNECT_POST = 1;

    MscHttpRequest mConnecter = null;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jsontest);

        textView = (TextView) findViewById(R.id.tv_jsontest);
        Worker mWorker = new Worker();
        // mWorker.start();
        // mWorker.postRunnable(request, 2000);

        new Thread(new Runnable() {

            @Override
            public void run() {
//                processData(URL_PATH);
            	processData1();
            }
        }).start();

    }

    public void processData(String mUrl) {

        HttpURLConnection urlConn = null;
        InputStream in = null;
        try {
            LogUtil.d("Start connect server");
            URL url = new URL(mUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(mTimeOut);
            urlConn.setReadTimeout(mTimeOut);
            urlConn.setRequestMethod("GET");

            int responseCode = urlConn.getResponseCode();
            LogUtil.d("responseCode = " + responseCode);
            if (HttpURLConnection.HTTP_OK == responseCode) {
                BufferedReader bf;
                in = urlConn.getInputStream();
                try {
                    bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    LogUtil.d("data read start : \n" + bf.toString());
                    while ((line = bf.readLine()) != null) {
                        buffer.append(line);
                    }
                    LogUtil.d("data read end : \n" + buffer.toString());
                    // String data = JsonTools.getHDPJson(msg);
                    // String data = JsonTools.getFyzbJson(buffer.toString());
                   JsonTools.getTvLive(mUrl, buffer.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                LogUtil.d("MscHttpRequest connect error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (urlConn != null) {
                    urlConn.disconnect();
                    urlConn = null;
                }
            } catch (Exception e) {
            }
        }

    }

    Runnable request = new Runnable() {

        @Override
        public void run() {
            processData1();
        }

    };

    private void processData1() {
        HttpURLConnection urlConn = null;
        InputStream in = null;
        try {
            LogUtil.d("Start connect server");
            URL url = new URL(URL_PATH);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(mTimeOut);
            urlConn.setReadTimeout(mTimeOut);
            urlConn.setRequestMethod("GET");

            int responseCode = urlConn.getResponseCode();
            LogUtil.d("responseCode = " + responseCode);
            if (HttpURLConnection.HTTP_OK == responseCode) {
                BufferedReader bf;
                in = urlConn.getInputStream();
                // JsonTools.getRiversFromXml(in);
                JsonTools.getRiversFromXml(in);

//                try {
//                    bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//                    StringBuffer buffer = new StringBuffer();
//                    String line = "";
//                    while ((line = bf.readLine()) != null) {
//                        System.out.println("line : " + line + ";  len : "
//                                + line.length());
//                        buffer.append(line);
//                    }
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            } else {
                LogUtil.d("MscHttpRequest connect error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (urlConn != null) {
                    urlConn.disconnect();
                    urlConn = null;
                }
            } catch (Exception e) {
            }
        }
    }

    class Worker extends Thread {

        private Handler mHandler;

        @Override
        public void run() {
            super.run();

            Looper.prepare();
            System.out.println("thread run");
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                }
            };
            Looper.loop();
        }

        public void upLoadScene(int delay) {
            if (mHandler == null)
                return;
            mHandler.sendEmptyMessageDelayed(UPLOAD_SCENE, delay);
        }

        public void upLoadChannellist(int delay) {
            if (mHandler == null)
                return;
            // mHandler.sendEmptyMessageDelayed(UPLOAD_CHANNEL_LIST, delay);
            // mHandler.sendEmptyMessageDelayed(, delayMillis)
        }

        public void upLoadChannelState(int delay) {
            if (mHandler == null)
                return;
            mHandler.sendEmptyMessageDelayed(UPLOAD_CHANNEL_STATE, delay);
        }

        public void postRunnable(Runnable r, int delay) {
            while (mHandler == null) {
                try {
                    System.out.println("wait for thread create");
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (mHandler != null) {
                System.out.println("postRunnable");
                mHandler.postDelayed(r, delay);
            } else {
                System.out.println("mHandler is null");
            }
        }

        public void uploadFeedback(String text, int type, int delay) {
            Message msg = Message.obtain();
            msg.obj = text;
            msg.arg1 = type;
            msg.what = UPLOAD_FEEDBACK;
            mHandler.sendMessageDelayed(msg, delay);
        }

        private static final int UPLOAD_SCENE = 0x1000;
        private static final int UPLOAD_CHANNEL_LIST = 0x1001;
        private static final int UPLOAD_CHANNEL_STATE = 0x1002;
        private static final int UPLOAD_FEEDBACK = 0x1003;

    }

}
