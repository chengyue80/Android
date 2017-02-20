package com.yue.demo.other;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iflytek.android.framework.toast.BaseToast;
import com.yue.demo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 系统闹钟、日历提醒:
 * 1、需要闹钟权限<uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
 * 2、启动通知action ：ACTION_SHOW_ALARMS (显示所有闹钟的列表)；ACTION_SET_ALARM(设置闹钟)
 * 3、调用系统闹钟不能设置超过24小时
 * <p/>
 * 一般来说实现向系统日历中读写事件：
 * 1、需要有读写日历权限
 * <uses-permission android:name="android.permission.READ_CALENDAR" />
 * <uses-permission android:name="android.permission.WRITE_CALENDAR" />
 * 2、如果没有日历账户需要先创建账户
 * 3、实现日历事件增删改查、提醒功能
 *
 * @author : chengyue
 * @version : v1.0
 * @createTime : 17/2/13
 * @history : change on v1.0
 */
public class AlarmClockDemo extends Activity {
    //新建闹钟
    private Button alarmBtn;
    //查看闹钟
    private Button alarmViewBtn;
    //日历
    private Button calBtn;
    //日历
    private Button calQueryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmBtn = (Button) findViewById(R.id.button1);
        alarmViewBtn = (Button) findViewById(R.id.button2);
        calBtn = (Button) findViewById(R.id.button3);
        calQueryBtn = (Button) findViewById(R.id.button4);
        alarmBtn.setText("新建闹钟");
        alarmViewBtn.setText("查看闹钟");
        calBtn.setText("获取账户id");
        calQueryBtn.setText("日历提醒");

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                c.add(Calendar.MINUTE, 1);

                if (System.currentTimeMillis() > c.getTimeInMillis()) {
                    BaseToast.DefaultToast(AlarmClockDemo.this, "不能设置当前时间之前的闹钟", 3000);
                    return;
                } else if ((c.getTimeInMillis() - System.currentTimeMillis()) > 24 * 60 * 60 * 1000) {
                    BaseToast.DefaultToast(AlarmClockDemo.this, "不能设置超过24小时的闹钟", 3000);
                    return;
                }

                ArrayList<Integer> days = new ArrayList<>();
                days.add(Calendar.SUNDAY);
                days.add(Calendar.MONDAY);
                days.add(Calendar.TUESDAY);
                //设置闹钟的action
                Intent openNewAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                //设置在几点提醒 24小时制
                openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, c.get(Calendar.HOUR_OF_DAY));
                //设置在几点提醒  12小时制
                // openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, c.get(Calendar.HOUR));
                //设置在几分提醒
                openNewAlarm.putExtra(AlarmClock.EXTRA_MINUTES, c.get(Calendar.MINUTE));
                openNewAlarm.putExtra(AlarmClock.EXTRA_DAYS, days);
                openNewAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, "闹钟内容");
                openNewAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                openNewAlarm.putExtra(AlarmClock.EXTRA_VIBRATE, true);//默认true
                startActivity(openNewAlarm);
            }
        });

        alarmViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                startActivity(view);

            }
        });

        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCalendarAccount(AlarmClockDemo.this);

            }
        });
        calQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCalendarEvent(AlarmClockDemo.this,"title","desc", System.currentTimeMillis() + 10* 1000, System.currentTimeMillis() + 20* 1000);


            }
        });


    }


    /**
     * 系统日历
     */
    public void setCalendar() {
        //1、申请权限
        //<uses-permission android:name="android.permission.READ_CALENDAR" />
        //<uses-permission android:name="android.permission.WRITE_CALENDAR" />
    }

    //添加日历相关的uri
    private static String CALANDER_URL = "content://com.android.calendar/calendars";
    private static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";

    /**
     * 检查是否有现有存在的账户。存在则返回账户id，否则返回-1
     *
     * @param context
     * @return
     */
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                int id = userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
                Log.d("Calendar","name : " + userCursor.getString(userCursor.getColumnIndex(CalendarContract.Calendars.NAME)));
                while (userCursor.moveToNext()) {
                    Log.d("Calendar","name : " + userCursor.getString(userCursor.getColumnIndex(CalendarContract.Calendars.NAME)));
                }

                return id;
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加账户。账户创建成功则返回账户id，否则返回-1
     *
     * @param context
     * @return
     */
    private static long addCalendarAccount(Context context) {
        String calendarsName = "test";
        String accountName = "test@gmail.com";
        String accountType = "com.android.exchange";
        String displayName = "测试账户";
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, calendarsName);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, accountType);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, displayName);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALANDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     *
     * @param context
     * @return 账户id
     */
    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }


    /**
     * 添加日历事件、日程
     *
     * @param context
     * @param title       日历标题
     * @param description 日历内容
     * @param beginTime   日历开始时间
     * @param endTime     日历结束时间
     */
    public static void addCalendarEvent(Context context, String title, String description, long beginTime, long
            endTime) {
        // 获取日历账户的id
        int calId = checkAndAddCalendarAccount(context);
        if (calId < 0) {
            // 获取账户id失败直接返回，添加日历事件失败
            Toast.makeText(context, "添加日历事件失败", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, title);
        event.put(CalendarContract.Events.DESCRIPTION, description);
        // 插入账户的id
        event.put(CalendarContract.Events.CALENDAR_ID, calId);

        //设置开始时间
        event.put(CalendarContract.Events.DTSTART, beginTime);
        //设置终止时间
        event.put(CalendarContract.Events.DTEND, endTime);
        //设置有闹钟提醒
        event.put(CalendarContract.Events.HAS_ALARM, true);
        //设置时区，必须有，
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALANDER_EVENT_URL), event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            Log.d("Calendar", "添加日历事件失败");
            return;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        // 提前10分钟有提醒
        values.put(CalendarContract.Reminders.MINUTES, 10);
        //提醒方式 （闹钟、信息、邮件。。。）
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALANDER_REMIDER_URL), values);
        if (uri == null) {
            // 添加闹钟提醒失败直接返回
            Log.d("Calendar", "添加闹钟提醒失败");
            return;
        }
    }

    /**
     * 删除日历事件、日程
     *
     * @param context
     * @param title   根据设置的title来查找并删除
     */
    public static void deleteCalendarEvent(Context context, String title) {
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALANDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null)//查询返回空值
                return;
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events
                            .TITLE));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALANDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) {
                            //事件删除失败
                            Log.d("Calendar", "日历事件删除失败");
                            return;
                        }
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }
}
