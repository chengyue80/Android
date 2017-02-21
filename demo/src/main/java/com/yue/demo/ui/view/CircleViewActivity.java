package com.yue.demo.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iflytek.android.framework.annotation.ViewInject;
import com.iflytek.android.framework.base.BaseFragment;
import com.yue.demo.R;
import com.yue.demo.RootActivity;

/**
 * 
 * @author chengyue
 * 
 */
public class CircleViewActivity extends RootActivity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new MainFragment()).commit();
		}
	}

	public static class MainFragment extends BaseFragment {
		@ViewInject(id = R.id.circleImage)
		private CircleImageView imageView;
		public MainFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_circle,
					container, false);
			imageView  = (CircleImageView) rootView.findViewById(R.id.circleImage);
			imageView.setmFillWidth(20);
			imageView.setImageResource(R.drawable.aaa);
			return rootView;
		}
	}

}
