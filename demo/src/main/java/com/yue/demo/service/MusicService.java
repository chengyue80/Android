package com.yue.demo.service;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore.Audio.Media;

import com.yue.demo.util.LogUtil;

public class MusicService extends Service {

    static final String KEY_CURRPLAYER = "musicId";
    static final String KEY_UPDATE = "updateStatus";
    static final String KEY_TOTALTIME = "totalTime";
    static final String KEY_CURRENTTIME = "currentTime";
    MyReceiver serviceReceiver;
    // AssetManager am;
    private int[] ids = { 0, 1, 2 };
    // MediaPlayer mPlayer;
    /**
     * 当前的状态,0x11 代表没有播放 ；0x12代表 正在播放；0x13代表暂停
     */
    int status = 0x11;
    /**
     * 记录当前正在播放的音乐
     */
    int position = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        LogUtil.d(MusicBox.tag, "----MusicService onCreate----");
        super.onCreate();
        // am = getAssets();
        serviceReceiver = new MyReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicBox.CTL_ACTION);
        registerReceiver(serviceReceiver, filter);

        // 创建MediaPlayer
        // 为MediaPlayer播放完成事件绑定监听器
        MusicBox.getPlayer().setOnCompletionListener(
                new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if (position >= ids.length)
                            position = 0;

                        LogUtil.d(MusicBox.tag, "OnCompletionListener");
                        Intent intent = new Intent(MusicBox.UPDATE_ACTION);

                        intent.putExtra(KEY_CURRPLAYER, position);
                        // 发送广播 ，将被Activity组件中的BroadcastReceiver接收到
                        sendBroadcast(intent);
                        // 准备、并播放音乐
                        prepareAndPlay(ids[position]);
                    }
                });

    }

    /**
     * 准备播放音乐
     * 
     * @param position
     *            所要播放的音乐在歌曲列表的position
     */
    protected void prepareAndPlay(int position) {
        // 打开指定音乐文件
        LogUtil.d(MusicBox.tag, "prepareAndPlay position : " + position);
        try {
            // AssetFileDescriptor afd = am.openFd(musicName);

            if (MusicBox.getPlayer().isPlaying())
                MusicBox.getPlayer().stop();
            Uri uri = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI,
                    String.valueOf(position));
            MusicBox.getPlayer().reset();
            // mPlayer.setDataSource(afd.getFileDescriptor(),
            // afd.getStartOffset(), afd.getLength());
            MusicBox.getPlayer().setDataSource(this, uri);
            MusicBox.getPlayer().prepare();
            MusicBox.getPlayer().start();

            // 发送广播通知Activity更新总时间
            Intent intent = new Intent(MusicBox.UPDATE_ACTION);
            intent.putExtra(KEY_UPDATE, 0x14);
            intent.putExtra(KEY_TOTALTIME, MusicBox.getPlayer().getDuration());
            // 发送广播 ，将被Activity组件中的BroadcastReceiver接收到
            sendBroadcast(intent);

            handler.sendEmptyMessage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新activity的time
     */
    Handler handler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Intent intent = new Intent(MusicBox.UPDATE_ACTION);
            intent.putExtra(KEY_UPDATE, 0x15);
            intent.putExtra(KEY_CURRENTTIME, MusicBox.getPlayer()
                    .getCurrentPosition());
            sendBroadcast(intent);
            handler.sendEmptyMessageDelayed(1, 1000);
            return false;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(MusicBox.tag, "----MusicService onStartCommand----");
        ids = intent.getIntArrayExtra("ids");// 歌曲name
        position = intent.getIntExtra(MusicBox.KEY_POSITION, -1);// 歌曲id
        LogUtil.d(MusicBox.tag, "titles ： " + ids.toString() + "\nposition : "
                + position);
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtil.d(MusicBox.tag,
                    "MusicService onReceive intent : "
                            + Uri.decode(intent.toUri(0)));
            int control = intent.getIntExtra(MusicBox.KEY_PLAYSTATUE, -1);
            switch (control) {
            // 播放或暂停
            case 1:
                // 原来处于没有播放状态
                if (status == 0x11) {
                    prepareAndPlay(ids[position]);

                    status = 0x12;
                }
                // 原来处于播放状态
                else if (status == 0x12) {
                    MusicBox.getPlayer().pause();
                    handler.removeMessages(1);
                    status = 0x13;
                }
                // 原来处于暂停状态
                else if (status == 0x13) {
                    MusicBox.getPlayer().start();
                    handler.sendEmptyMessage(1);
                    status = 0x12;
                }
                break;
            // 停止播放
            case 2:
                // 如果原来正在播放或暂停
                if (status == 0x12 || status == 0x13) {
                    Intent intent2 = new Intent(MusicBox.UPDATE_ACTION);
                    intent2.putExtra(KEY_UPDATE, 0x15);
                    intent2.putExtra(KEY_CURRENTTIME, 0);
                    sendBroadcast(intent2);

                    MusicBox.getPlayer().stop();
                    handler.removeMessages(1);
                    status = 0x11;
                }
                break;
            // 下一曲
            case 3:

                position--;
                if (position < 0) {
                    position = ids.length - 1;
                }
                prepareAndPlay(ids[position]);
                status = 0x12;
                break;
            // 上一曲
            case 4:
                position++;
                if (position >= ids.length) {
                    position = 0;
                }
                prepareAndPlay(ids[position]);
                status = 0x12;

                break;

            case 5:
                int progress = intent.getIntExtra("progress", -1);
                MusicBox.getPlayer().seekTo(progress);
                break;

            }
            LogUtil.d(MusicBox.tag, "MusicService status : " + status);
            // 发送广播通知Activity更改图标、文本框
            Intent update = new Intent(MusicBox.UPDATE_ACTION);
            update.putExtra(KEY_UPDATE, status);
            update.putExtra(KEY_CURRPLAYER, position);
            sendBroadcast(update);

        }

    }
}
