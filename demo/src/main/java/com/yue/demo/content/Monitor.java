package com.yue.demo.content;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 监听用户发出的短信，最好采用service来实现<br/>
 * 监听手机来电
 * 
 * @author chengyue
 * 
 */
public class Monitor extends Activity {

    private final String tag = Monitor.class.getSimpleName();
    // 记录黑名单的List
    ArrayList<String> blockList = new ArrayList<String>();
    TelephonyManager tManager;
    SmsManager smsManager;
    // 监听通话状态的监听器
    CustomPhoneCallListener cpListener;

    private Button button, sendSms, groupSms;
    private EditText phoneNum, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        // 为content://sms的数据改变注册监听器
        getContentResolver().registerContentObserver(
                Uri.parse("content://sms"), true,
                new SmsObserver(new Handler()));

        tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        /**
         * 监听来电
         */
        PhoneStateListener listener = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                switch (state) {
                // 无状态
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    OutputStream os = null;

                    try {
                        os = openFileOutput("phoneList", MODE_APPEND);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    PrintStream ps = new PrintStream(os);
                    ps.println(new Date() + " 来电 ： " + incomingNumber);
                    ps.close();

                    break;

                default:
                    break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }

        };
        tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        /**
         * 监听电话黑名单
         */
        cpListener = new CustomPhoneCallListener();
        // 通过TelephonyManager监听通话状态的改变
        tManager.listen(cpListener, PhoneStateListener.LISTEN_CALL_STATE);
        // 获取程序的按钮，并为它的单击事件绑定监听器
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查询联系人的电话号码
                final Cursor cursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, null);
                BaseAdapter adapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return cursor.getCount();
                    }

                    @Override
                    public Object getItem(int position) {
                        return position;
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView,
                            ViewGroup parent) {
                        cursor.moveToPosition(position);
                        CheckBox rb = new CheckBox(Monitor.this);
                        // 获取联系人的电话号码，并去掉中间的中划线、空格
                        String number = cursor
                                .getString(
                                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                .replace("-", "").replace(" ", "");
                        rb.setText(number);
                        // 如果该号码已经被加入黑名单、默认勾选该号码
                        if (isChecked(number)) {
                            rb.setChecked(true);
                        }

                        return rb;
                    }
                };
                // 加载list.xml布局文件对应的View
                View selectView = getLayoutInflater().inflate(
                        R.layout.listview, null);
                // 获取selectView中的名为list的ListView组件
                final ListView listView = (ListView) selectView
                        .findViewById(R.id.listview);
                listView.setAdapter(adapter);

                // cursor.close();
                new AlertDialog.Builder(Monitor.this)
                        .setView(selectView)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        // 清空blockList集合
                                        blockList.clear();
                                        // 遍历listView组件的每个列表项
                                        for (int i = 0; i < listView.getCount(); i++) {
                                            CheckBox checkBox = (CheckBox) listView
                                                    .getChildAt(i);
                                            // 如果该列表项被勾选
                                            if (checkBox.isChecked()) {
                                                // 添加该列表项的电话号码
                                                blockList.add(checkBox
                                                        .getText().toString());
                                            }
                                        }
                                        phoneNum.setText(blockList.toString());
                                        LogUtil.i(tag, "黑名单 ： " + blockList);
                                    }
                                }).show();
            }
        });

        smsManager = SmsManager.getDefault();
        sendSms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String num = phoneNum.getText().toString();
                String con = (content.getText().toString() == null || content
                        .getText().toString().trim().equals("")) ? "" : content
                        .getText().toString();

                PendingIntent pIntent = PendingIntent.getActivity(Monitor.this,
                        0, new Intent(), 0);
                // 发送短信
                smsManager.sendTextMessage(num, null, con, pIntent, null);
                Toast.makeText(Monitor.this, "短信发送完成", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        groupSms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String con = (content.getText().toString() == null || content
                        .getText().toString().trim().equals("")) ? "" : content
                        .getText().toString();
                for (int i = 0; i < blockList.size(); i++) {
                    PendingIntent pIntent = PendingIntent.getActivity(
                            Monitor.this, 0, new Intent(), 0);
                    // 发送短信
                    smsManager.sendTextMessage(blockList.get(i), null, con,
                            pIntent, null);
                }

                Toast.makeText(Monitor.this, "短信群发发送完成", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    /**
     * 判断某个电话号码是否在黑名单之内
     * 
     * @param phoneNum
     * @return
     */

    public boolean isChecked(String phoneNum) {
        // Mlog.i(tag, "呼入号码：" + phone);
        // Mlog.i(tag, "--------" + blockList);

        for (String s1 : blockList) {
            if (s1.equals(phoneNum)) {
                return true;
            }
        }
        return false;
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        button = new Button(this);
        button.setLayoutParams(MainActivity.layoutParams_mw);
        button.setText("设置黑名单或选择收件人");
        ll.addView(button);

        phoneNum = new EditText(this);
        phoneNum.setLayoutParams(MainActivity.layoutParams_mw);
        phoneNum.setHint("收件人号码 ：");
        ll.addView(phoneNum);

        content = new EditText(this);
        content.setLayoutParams(MainActivity.layoutParams_mw);
        content.setHint("短信内容 ： ");
        ll.addView(content);

        sendSms = new Button(this);
        sendSms.setLayoutParams(MainActivity.layoutParams_mw);
        sendSms.setText("发送短信");
        ll.addView(sendSms);

        groupSms = new Button(this);
        groupSms.setLayoutParams(MainActivity.layoutParams_mw);
        groupSms.setText("群发短信");
        ll.addView(groupSms);

        setContentView(ll);
    }

    /**
     * 提供自定义的ContentObserver监听器类
     * 
     * @author chengyue
     * 
     */
    private final class SmsObserver extends ContentObserver {

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            LogUtil.d(tag, "----SmsObserver onChange start----");
            // super.onChange(selfChange);
            // 查询发送箱中的短信(处于正在发送状态的短信放在发送箱)
            Cursor cursor = getContentResolver().query(
                    Uri.parse("content://sms/outbox"), null, null, null, null);
            // 遍历查询得到的结果集，即可获取用户正在发送的短信
            while (cursor.moveToNext()) {
                StringBuffer sb = new StringBuffer();
                // 获取短信的发送地址
                sb.append("address=").append(
                        cursor.getString(cursor.getColumnIndex("address")));
                // 获取短信的标题
                sb.append(";subject=").append(
                        cursor.getString(cursor.getColumnIndex("subject")));
                // 获取短信的内容
                sb.append(";body=").append(
                        cursor.getString(cursor.getColumnIndex("body")));
                // 获取短信的发送时间
                sb.append(";time=").append(
                        cursor.getLong(cursor.getColumnIndex("date")));
                LogUtil.d(tag, "Has Sent SMS::\n" + sb.toString());
                Toast.makeText(Monitor.this, "正在发送短信。。。\n" + sb.toString(),
                        Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            LogUtil.d(tag, "----SmsObserver onChange end----");
        }

    }

    /**
     * 黑名单
     * 
     * @author chengyue
     * 
     */
    public class CustomPhoneCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String number) {
            switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
            // 当电话呼入时
            case TelephonyManager.CALL_STATE_RINGING:
                // 如果该号码属于黑名单
                if (isChecked(number)) {
                    LogUtil.i(tag, "～～～挂断电话～～～");
                    try {
                        Method method = Class.forName(
                                "android.os.ServiceManager").getMethod(
                                "getService", String.class);
                        // 获取远程TELEPHONY_SERVICE的IBinder对象的代理
                        IBinder binder = (IBinder) method.invoke(null,
                                new Object[] { TELEPHONY_SERVICE });
                        // 将IBinder对象的代理转换为ITelephony对象
                        ITelephony telephony = ITelephony.Stub
                                .asInterface(binder);
                        // 挂断电话
                        telephony.endCall();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            super.onCallStateChanged(state, number);
        }
    }
}
