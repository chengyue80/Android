package com.yue.demo.jsontest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.yue.demo.R;

public class JsonTest extends Activity {

    public final String DATA_PATH = "http://dsjapi.duapp.com/api/channel/list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        getNewsFromJson();
                        // String str = getJsonString("channelList");
                        String str = getNewsFromJson();
                        // System.out.println("josn  数据 = " + str);
                        // JSONObject jsonObject = new JSONObject(str);
                        JSONArray jsonArray = new JSONArray(str);
                        System.out.println("jsonArray.length()"
                                + jsonArray.length());
                        // JSONArray channels =
                        // jsonObject.getJSONArray("channels");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray channels = jsonArray.getJSONObject(i)
                                    .getJSONArray("channels");
                            String chineseName = jsonArray.getJSONObject(i)
                                    .getString("chineseName");
                            System.out.println("chineseName = " + chineseName
                                    + "  channelListArray.length()"
                                    + channels.length());
                            for (int j = 0; j < channels.length(); j++) {
                                String name = channels.getJSONObject(j)
                                        .getString("name");
                                System.out.println("name = " + name);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            inputStream = new InputStreamReader(getResources().getAssets()
                    .open(channelList));
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

    /**
     * 获取最新视频资讯，从JSON文件中，解析效率高
     * 
     * @return
     * 
     * @return
     * @throws Exception
     */
    public String getNewsFromJson() throws Exception {
        String path = DATA_PATH;
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
        String jsonStr = null;
        if (200 == conn.getResponseCode()) {

            InputStream instream = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = instream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            byte[] data = bos.toByteArray();

            // BufferedInputStream bis = new
            // BufferedInputStream(conn.getInputStream());
            // ByteArrayBuffer baf = new ByteArrayBuffer(128);
            // int current = 0;
            // while ((current = bis.read()) != -1) {
            // baf.append((byte) current);
            // }
            //
            // byte[] data = baf.toByteArray();

            jsonStr = new String(data);
            // System.out.println("======" + jsonStr );
        }
        return processData(jsonStr);
    }

    public String processData(String msg) throws Exception {
        String data = "";

        JSONTokener toker = new JSONTokener(msg);
        JSONObject obj = new JSONObject(toker);
        JSONArray actions = obj.getJSONArray("actions");

        data = actions.getJSONObject(0).getString("data").toString();
        System.out.println("data = " + data);
        return data;
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
     * 访问数据库并返回JSON数据字符串
     * 
     * @param params
     *            向服务器端传的参数
     * @param url
     * @return
     * @throws Exception
     */
    public static String doPost(List<NameValuePair> params, String url)
            throws Exception {
        String result = null;
        // 获取HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        // 新建HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            // 设置字符集
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            // 设置参数实体
            httpPost.setEntity(entity);
        }

        /*
         * // 连接超时 httpClient.getParams().setParameter(
         * CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); // 请求超时
         * httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
         * 3000);
         */
        // 获取HttpResponse实例
        HttpResponse httpResp = httpClient.execute(httpPost);
        // 判断是够请求成功
        if (httpResp.getStatusLine().getStatusCode() == 200) {
            // 获取返回的数据
            result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
        } else {
            Log.i("HttpPost", "HttpPost方式请求失败");
        }

        return result;
    }

}
