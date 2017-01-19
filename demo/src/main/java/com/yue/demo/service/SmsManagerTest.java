package com.yue.demo.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yue.demo.content.Monitor;

/**
 * 短信管理器
 * 
 * @author chengyue
 * 
 */
public class SmsManagerTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SmsManagerTest.this, Monitor.class));
    }

}
