package com.wxcily.xunplayer.player.widght;

import com.wxcily.xunplayer.player.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyProgressBar extends ImageView {

	public MyProgressBar(Context context) {
		super(context);
		init(context);
	}

	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.setImageDrawable(context.getResources().getDrawable(R.anim.anim_loading));
		AnimationDrawable animationDrawable = (AnimationDrawable) this
				.getDrawable();
		animationDrawable.start();
	}
}
