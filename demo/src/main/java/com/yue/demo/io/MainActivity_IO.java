package com.yue.demo.io;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainActivity_IO extends LauncherActivity {
	protected static final String Tag = "data >> ";

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		String[] mActivity = { "OtherSharePreferencetest", "FileTest",
				"SDCardTest", "SDFileExplorer", "DBTest", "Dict",
				"GestrueTest", "AddGestrue", "RecogniseGestrue", "Speech",
				"AssetDemo" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mActivity);
		setListAdapter(adapter);

	}

	@Override
	protected Intent intentForPosition(int position) {

		switch (position) {
		case 0:
			return new Intent(MainActivity_IO.this,
					OtherSharePreferencetest.class);
		case 1:
			return new Intent(MainActivity_IO.this, FileTest.class);

		case 2:
			return new Intent(MainActivity_IO.this, SDCardTest.class);

		case 3:
			return new Intent(MainActivity_IO.this, SDFileExplorer.class);

		case 4:
			return new Intent(MainActivity_IO.this, DBTest.class);

		case 5:
			return new Intent(MainActivity_IO.this, Dict.class);

		case 6:
			return new Intent(MainActivity_IO.this, GestrueTest.class);
		case 7:
			return new Intent(MainActivity_IO.this, AddGestrue.class);
		case 8:
			return new Intent(MainActivity_IO.this, RecogniseGestrue.class);
		case 9:
			return new Intent(MainActivity_IO.this, Speech.class);
		case 10:
			return new Intent(MainActivity_IO.this, AssetDemo.class);

		default:
			return super.intentForPosition(position);
		}

	}

}
