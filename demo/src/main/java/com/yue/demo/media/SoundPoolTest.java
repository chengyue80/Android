package com.yue.demo.media;

import java.util.HashMap;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yue.demo.MainActivity;
import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 使用soundpool播放音效
 * 
 * @author chengyue
 * 
 */
public class SoundPoolTest extends Activity {

    private Button bomb, shot, arrow;

    private SoundPool pool;
    private HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        // 设置最多可容纳10个音频流，音频的品质为5
        pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        // load方法加载指定音频文件，并返回所加载的音频ID。
        // 此处使用HashMap来管理这些音频流
        soundMap.put(1, pool.load(this, R.raw.shake_phone, 1));
        soundMap.put(2, pool.load(this, R.raw.shake_good, 1));
        soundMap.put(3, pool.load(this, R.raw.shake_nogood, 1));
        
        pool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				// TODO Auto-generated method stub
				LogUtil.d("pool", "sampleId/status : " + sampleId + "/" +status);
				
			}
		});

        bomb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pool.play(soundMap.get(1), 1, 1, 0, 0, 1);

            }
        });
        shot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pool.play(soundMap.get(2), 1, 1, 0, 0, 1);

            }
        });
        arrow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pool.play(soundMap.get(3), 1, 1, 0, 0, 1);

            }
        });

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        bomb = new Button(this);
        bomb.setLayoutParams(MainActivity.layoutParams_mw);
        bomb.setText("爆炸声");
        ll.addView(bomb);

        shot = new Button(this);
        shot.setLayoutParams(MainActivity.layoutParams_mw);
        shot.setText("射击声");
        ll.addView(shot);

        arrow = new Button(this);
        arrow.setLayoutParams(MainActivity.layoutParams_mw);
        arrow.setText("射箭声");
        ll.addView(arrow);

        setContentView(ll);
    }

}
