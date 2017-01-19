package com.iflytek.android.framework.base.v4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.iflytek.android.framework.annotation.BaseInject;

public class BaseFragment extends Fragment {

	/**
	 * 初始化fragment时完成注解功能
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initBaseInject(this);
		super.onActivityCreated(savedInstanceState);
	}

	public void initBaseInject(Fragment fragment) {
		BaseInject.getInstance().initInject(fragment);
		BaseInject.getInstance().eventInject(fragment);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		BaseInject.getInstance().unEventInject(getActivity());
	}
}