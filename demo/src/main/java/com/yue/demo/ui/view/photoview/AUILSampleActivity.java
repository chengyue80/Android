package com.yue.demo.ui.view.photoview;

import android.app.Activity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photoview.PhotoView;
import com.yue.demo.R;

public class AUILSampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoview_simple);

		PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

		if (!ImageLoader.getInstance().isInited()) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					getApplicationContext()).build();
			ImageLoader.getInstance().init(config);
		}

		ImageLoader.getInstance().displayImage(
				"http://pbs.twimg.com/media/Bist9mvIYAAeAyQ.jpg", photoView);
	}
}
