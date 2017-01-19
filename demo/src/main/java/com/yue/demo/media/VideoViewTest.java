package com.yue.demo.media;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.yue.demo.MainActivity;

/**
 * 使用VideoView播放视频
 * 
 * @author chengyue
 * 
 */
public class VideoViewTest extends Activity {

    private VideoView videoView;
    private MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        controller = new MediaController(this);
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/DCIM/Camera/videoviewtest.mp4");

        if (file.exists()) {

            // 加载指定视频文件
            videoView.setVideoPath(file.getAbsolutePath());
            // 与MediaController进行关联，获取系统的播放控制（快进、快退等）
            videoView.setMediaController(controller);
            controller.setMediaPlayer(videoView);
            // 获取焦点
            videoView.requestFocus();
            
        } else {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        videoView = new VideoView(this);
        videoView.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(videoView);

        setContentView(ll);

    }

}
