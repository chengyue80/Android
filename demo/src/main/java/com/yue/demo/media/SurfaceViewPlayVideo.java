package com.yue.demo.media;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.graphics.SurfaceViewTest;
import com.yue.demo.util.LogUtil;

/**
 * 使用SurfaceView播放视频
 * 
 * @author chengyue
 * 
 */
public class SurfaceViewPlayVideo extends Activity {

    protected final String tag = SurfaceViewPlayVideo.class.getSimpleName();
    private SurfaceView surfaceView;

    private ImageButton play, pause, stop;
    private SurfaceHolder holder;
    private Button turn;
    MediaPlayer mPlayer;
    // 记录当前视频的播放位置
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        mPlayer = new MediaPlayer();
        holder = surfaceView.getHolder();
        holder.setKeepScreenOn(true);
        holder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (position > 0) {
                    play();
                    mPlayer.seekTo(position);
                    position = 0;
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
                LogUtil.d(tag, "surfaceChanged");

            }
        });

        turn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(SurfaceViewPlayVideo.this,
                        SurfaceViewTest.class));
            }
        });

        play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                play();

            }

        });
        pause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                } else
                    mPlayer.start();

            }
        });
        stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPlayer.isPlaying())
                    mPlayer.stop();

            }
        });
    }

    private void play() {
        try {
            mPlayer.reset();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/DCIM/Camera/videoviewtest.mp4");
            // 把视频画面输出到SurfaceView
            mPlayer.setDisplay(holder);
            mPlayer.prepare();

//            WindowManager manager = getWindowManager();
//            DisplayMetrics metrics = new DisplayMetrics();
//            Display display = manager.getDefaultDisplay();
//            display.getMetrics(metrics);
//            surfaceView.setLayoutParams(new LayoutParams(metrics.widthPixels,
//                    mPlayer.getVideoHeight() * metrics.widthPixels
//                            / mPlayer.getVideoWidth()));
            mPlayer.start();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            position = mPlayer.getCurrentPosition();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }

        super.onDestroy();
    }

    private void initView() {

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        turn = new Button(this);
        turn.setLayoutParams(MainActivity.layoutParams_mw);
        turn.setText("使用SurfaceView实现绘画");
        ll.addView(turn);

        MainActivity.layoutParams_mm.weight = 1;
        surfaceView = new SurfaceView(this);
        surfaceView.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(surfaceView);

        LinearLayout ll_iv = new LinearLayout(this);
        ll_iv.setLayoutParams(MainActivity.layoutParams_mw);
        ll_iv.setOrientation(LinearLayout.HORIZONTAL);
        ll_iv.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.addView(ll_iv);

        play = new ImageButton(this);
        play.setLayoutParams(MainActivity.layoutParams_ww);
        play.setImageResource(R.drawable.play);
        ll_iv.addView(play);

        pause = new ImageButton(this);
        pause.setLayoutParams(MainActivity.layoutParams_ww);
        pause.setImageResource(R.drawable.pause);
        ll_iv.addView(pause);

        stop = new ImageButton(this);
        stop.setLayoutParams(MainActivity.layoutParams_ww);
        stop.setImageResource(R.drawable.stop);
        ll_iv.addView(stop);

        setContentView(ll);

    }
}
