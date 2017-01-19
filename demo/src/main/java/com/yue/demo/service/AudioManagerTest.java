package com.yue.demo.service;

import android.app.Activity;
import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * 音频管理器
 * 
 * @author chengyue
 * 
 */
public class AudioManagerTest extends Activity {

    private AudioManager aManager;
    MediaPlayer player;
    private Vibrator vibrator;
    private Button play, up, down;
    private ToggleButton mute;// 正常、静音

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        aManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);

        play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                player = MediaPlayer.create(AudioManagerTest.this, R.raw.earth);
                // 设置循环播放
                player.setLooping(true);
                player.start();
                play.setEnabled(false);

            }
        });
        up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // 指定调节音乐的音频，增大音量，而且显示音量图形示意
                aManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });
        down.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 指定调节音乐的音频，增大降低，而且显示音量图形示意
                aManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

            }
        });

        mute.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                aManager.setStreamMute(AudioManager.STREAM_MUSIC, isChecked);

            }
        });

        /**
         * 振动器
         */
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(getApplicationContext(), "手机震动", Toast.LENGTH_SHORT)
                .show();
        // 控制手机震动2秒
        vibrator.vibrate(2000);
        return super.onTouchEvent(event);
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        play = new Button(this);
        play.setLayoutParams(MainActivity.layoutParams_mw);
        play.setText("音频播放音乐");
        ll.addView(play);

        up = new Button(this);
        up.setLayoutParams(MainActivity.layoutParams_mw);
        up.setHint("音量增大");
        ll.addView(up);

        down = new Button(this);
        down.setLayoutParams(MainActivity.layoutParams_mw);
        down.setHint("音量减低");
        ll.addView(down);

        mute = new ToggleButton(this);
        mute.setLayoutParams(MainActivity.layoutParams_mw);
        mute.setTextOn("正常");
        mute.setTextOff("静音");
        ll.addView(mute);

        setContentView(ll);
    }

    @Override
    protected void onDestroy() {

        player.stop();
        super.onDestroy();
    }
}
