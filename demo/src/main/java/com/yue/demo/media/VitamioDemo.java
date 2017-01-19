package com.yue.demo.media;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wxcily.xunplayer.player.ui.VideoActivity;
import com.yue.demo.MainActivity;

/**
 * 集成Vitamio 播放视频
 * 
 * @author chengyue
 * @date 2015-5-6
 * @version 1.0
 */
public class VitamioDemo extends Activity {

	private EditText editText;
	private Button button;
	private String url = "http://pl.youku.com/playlist/m3u8?ts=1419841762&keyframe=0&vid=XODU1NDc4Mzg4&type=flv&ep=ciaVE0GJV8kG5yfYjT8bYyy3d3JcXJZ1gnqE%2F6YxBMZuH%2BzQnT3YwQ%3D%3D&sid=04198417624751240e35c&token=4396&ctype=12&ev=1&oip=3663591661";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String url = editText.getText().toString();
				String[] urls = new String[] { url };
				Intent intent = new Intent(VitamioDemo.this,
						VideoActivity.class);
				intent.putExtra("video_path", urls);
				intent.putExtra("video_name", "百元哥");
				VitamioDemo.this.startActivity(intent);

			}
		});

	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);

		editText = new EditText(this);
		editText.setLayoutParams(MainActivity.layoutParams_mw);
		editText.setText(url);
		ll.addView(editText);

		button = new Button(this);
		button.setLayoutParams(MainActivity.layoutParams_mw);
		button.setText("播放");
		ll.addView(button);

	}
}
