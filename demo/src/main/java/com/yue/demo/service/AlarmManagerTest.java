package com.yue.demo.service;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 手机闹钟服务
 * 
 * @author chengyue
 * 
 */
public class AlarmManagerTest extends Activity {

    private final String tag = AlarmManagerTest.class.getSimpleName();
    private AlarmManager aManager;
    PendingIntent pIntent;
    private Button play, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(AlarmManagerTest.this, AlarmActivity.class);
        // 创建PendingIntent对象,不能再定义相同的，否则无效
        pIntent = PendingIntent.getActivity(AlarmManagerTest.this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();

                // PendingIntent pIntent = PendingIntent.getActivity(
                // AlarmManagerTest.this, 0, intent,
                // PendingIntent.FLAG_CANCEL_CURRENT);

                // 设定一个五秒后的时间
                // Calendar calendar = Calendar.getInstance();
                // calendar.setTimeInMillis(System.currentTimeMillis());
                // calendar.add(Calendar.SECOND, 2);

                // System.out.println(calendar.getTimeInMillis());
                // aManager.set(AlarmManager.RTC_WAKEUP,
                // calendar.getTimeInMillis(), pi);

                // aManager.setRepeating(AlarmManager.RTC_WAKEUP,
                // System.currentTimeMillis() + 2000, 3000, pIntent);

                // aManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                // SystemClock.elapsedRealtime() + 2000, pIntent);

                // int year = currentTime.get(Calendar.YEAR);
                // int month = currentTime.get(Calendar.MONTH);
                // int dayofmonth = currentTime.get(Calendar.DAY_OF_MONTH);
                // int dayofweek = currentTime.get(Calendar.DAY_OF_WEEK);
                // int dayofweekinmonth = currentTime
                // .get(Calendar.DAY_OF_WEEK_IN_MONTH);
                // int day_of_year = currentTime.get(Calendar.DAY_OF_YEAR);
                // int hour = currentTime.get(Calendar.HOUR);
                // int hour_of_day = currentTime.get(Calendar.HOUR_OF_DAY);
                // int minute = currentTime.get(Calendar.MINUTE);

                // Mlog.i(tag, "==========\nyear : " + year + "\nmonth : " +
                // month
                // + "\ndayofmonth : " + dayofmonth + "\ndayofweek : "
                // + dayofweek + "\ndayofweekinmonth : "
                // + dayofweekinmonth + "\nday_of_year : " + day_of_year
                // + "\nhour : " + hour + "\nhour_of_day : " + hour_of_day
                // + "\nminite : " + minute + "\n============");

                // Mlog.d(tag, "currentTime : " + currentTime.getTime());
                // 创建一个TimePickerDialog实例，并把它显示出来。
                new TimePickerDialog(AlarmManagerTest.this, 0,
                        new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view,
                                    int hourOfDay, int minute) {

                                LogUtil.d(tag, "hourOfDay : " + hourOfDay
                                        + "   minute : " + minute);

                                Calendar c = Calendar.getInstance();
                                // 根据用户选择时间来设置Calendar对象
                                // c.set(Calendar.MONTH, Calendar.MONTH);
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                // 设置AlarmManager将在Calendar对应的时间启动指定组件
                                // System.out.println("c : " +
                                // c.getTimeInMillis());
                                System.out.println("c : " + c.getTime());
                                aManager.set(AlarmManager.RTC_WAKEUP,
                                        c.getTimeInMillis(), pIntent);
                                // 显示闹铃设置成功的提示信息
                                Toast.makeText(getApplicationContext(),
                                        "闹铃设置成功啦", Toast.LENGTH_SHORT).show();

                            }
                        }, currentTime.get(Calendar.HOUR), currentTime
                                .get(Calendar.MINUTE), false).show();
            }
        });

        stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                aManager.cancel(pIntent);
            }
        });

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        play = new Button(this);
        play.setLayoutParams(MainActivity.layoutParams_mw);
        play.setText("设置闹钟");
        ll.addView(play);

        stop = new Button(this);
        stop.setLayoutParams(MainActivity.layoutParams_mw);
        stop.setText("取消闹钟");
        ll.addView(stop);

        setContentView(ll);
    }
}
