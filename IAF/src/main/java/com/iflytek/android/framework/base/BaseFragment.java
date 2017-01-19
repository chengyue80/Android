package com.iflytek.android.framework.base;

import com.iflytek.android.framework.annotation.BaseInject;

import android.app.Fragment;
import android.os.Bundle;
/**
 * 基本Frament 界面初始化工作请再onActivityCreated中进行
 * com.iflytek.android.framework.base.BaseFragment
 * @author 陈智磊 <br/>
 * create at 2015年3月19日 上午9:34:05
 */
public class BaseFragment extends Fragment {
	/**
	 * 初始化fragment时完成注解功能
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initBaseInject(this);
		super.onActivityCreated(savedInstanceState);
	}
    
    public void initBaseInject(Fragment fragment) {
        BaseInject.getInstance().initInject(fragment);
        BaseInject.getInstance().eventInject(fragment);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	BaseInject.getInstance().unEventInject(getActivity());
    }
    
}
