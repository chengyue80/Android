package com.yue.demo.ui.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class SpinnerTest extends Activity {
    private final String Tag = "SpinnerTest >> ";

    private Spinner sp_province, sp_city;

    private ArrayList<String> list_province = new ArrayList<String>();
    private ArrayList<String> list_cities = new ArrayList<String>();
    private ArrayList<String> list_city = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinnertest);
        sp_province = (Spinner) findViewById(R.id.province);
        sp_city = (Spinner) findViewById(R.id.city);

        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        BufferedReader bf = null;
        try {
            inputStream = assetManager.open("provinces");
            bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
            LogUtil.d(Tag, "provinces : " + sb.toString());

            JSONObject object = new JSONObject(sb.toString());

            String provinces = object.getString("provinces");
            JSONArray array = new JSONArray(provinces);

            list_province.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject province = new JSONObject(array.optString(i));
                LogUtil.d(Tag, "province : " + province.getString("name"));
                // JSONArray cities = province.getJSONArray("cities");
                list_province.add(province.getString("name"));
                list_cities.add(province.getString("cities").toString());

                // Map<String, String> map = new HashMap<String, String>();
                // map.put(province.getString("name"),
                // province.getJSONArray("cities").toString());
                // map_city.add(map);
                // for (int j = 0; j < cities.length(); j++) {
                // Mlog.i(Tag, "city : " + cities.getString(j));
                //
                // }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        adapter = new ArrayAdapter<String>(
                SpinnerTest.this, android.R.layout.simple_spinner_item,
                list_city);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        sp_province.setAdapter(new ArrayAdapter<String>(SpinnerTest.this,
                android.R.layout.simple_list_item_1, list_province));
        sp_city.setAdapter(adapter);

        sp_province.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {

                try {
                    JSONArray cities = new JSONArray(list_cities.get(position));
                    list_city.clear();
                    for (int i = 0; i < cities.length(); i++) {
                        LogUtil.i(Tag, "city : " + cities.getString(i));
                        list_city.add(cities.getString(i));
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }
}
