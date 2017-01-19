package com.yue.demo.content;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 使用contentprovider管理多媒体内容<br/>
 * 可能会出现图片太大而无法显示的问题，<br/>
 * 在manifest的相应activity中设置hardwareAccelerated = false即可
 * 
 * @author chengyue
 * 
 */
public class MediaProviderTest extends Activity {

    private final String tag = "MediaProviderTest";

    private Button add, search;
    private ListView listView;

    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> descs = new ArrayList<String>();
    private ArrayList<String> filePaths = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 创建ContentValues对象，准备插入数据
                ContentValues values = new ContentValues();
                values.put(Media.DISPLAY_NAME, "jinta");
                values.put(Media.DESCRIPTION, "金塔");
                values.put(Media.MIME_TYPE, "image/jpeg");
                // 插入数据，返回所插入数据对应的Uri
                Uri uri = getContentResolver().insert(
                        Media.EXTERNAL_CONTENT_URI, values);
                LogUtil.d(tag, "insert uri : " + uri.toString());
                // 加载应用程序下的jinta图片
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.jinta);
                OutputStream os = null;

                try {
                    // 获取刚插入的数据的Uri对应的输出流
                    os = getContentResolver().openOutputStream(uri);
                    // 将bitmap图片保存到Uri对应的数据节点中
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 清空names.descs/fileNames的数据
                names.clear();
                descs.clear();
                filePaths.clear();
                // 通过ContentResolver查询所有图片信息
                Cursor cursor = getContentResolver().query(
                        Media.EXTERNAL_CONTENT_URI, null, null, null, null);

                while (cursor.moveToNext()) {
                    // 获取图片的显示名
                    String name = cursor.getString(cursor
                            .getColumnIndex(Media.DISPLAY_NAME));

                    // 获取图片的详细描述
                    String desc = cursor.getString(cursor
                            .getColumnIndex(Media.DESCRIPTION));
                    // 获取图片的保存位置的数据 Media.DATA = _data
                    byte[] bs = cursor.getBlob(cursor
                            .getColumnIndex(Media.DATA));
                    // 将图片名添加到names集合中
                    names.add(name);
                    descs.add(desc);
                    // 将图片保存路径添加到fileNames集合中
                    String path = new String(bs, 0, bs.length - 1);
                    filePaths.add(path);

                    LogUtil.d(tag, "name : " + name + ";  path : " + path
                            + ";   desc:" + desc);
                }
                // 创建一个List集合，List集合的元素是Map
                List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
                // 将names、descs两个集合对象的数据转换到Map集合中
                for (int i = 0; i < names.size(); i++) {

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", names.get(i));
                    map.put("desc", descs.get(i));
                    listItems.add(map);
                }

                // 创建一个SimpleAdapter
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        MediaProviderTest.this, listItems,
                        R.layout.listview_item,
                        new String[] { "name", "desc" }, new int[] {
                                R.id.listview_item_name,
                                R.id.listview_item_value });
                // 为 ListView组件设置Adapter
                listView.setAdapter(simpleAdapter);

            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                final ImageView imageView = new ImageView(
                        MediaProviderTest.this);
                imageView.setLayoutParams(MainActivity.layoutParams_mm);
                imageView.setScaleType(ScaleType.FIT_CENTER);
                imageView.setImageBitmap(BitmapFactory.decodeFile(filePaths
                        .get(position)));

                new AlertDialog.Builder(MediaProviderTest.this)
                        .setView(imageView).setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        add = new Button(this);
        add.setLayoutParams(MainActivity.layoutParams_ww);
        add.setText("add");
        ll.addView(add);

        search = new Button(this);
        search.setLayoutParams(MainActivity.layoutParams_ww);
        search.setText("search");
        ll.addView(search);

        listView = new ListView(this);
        listView.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(listView);

        setContentView(ll);
    }
}
