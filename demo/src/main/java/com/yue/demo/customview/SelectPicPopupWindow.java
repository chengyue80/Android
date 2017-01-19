package com.yue.demo.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yue.demo.R;

/**
 * 图片选择弹出窗口
 * 
 * @author Administrator
 * @datetime 2014年11月29日上午9:59:02
 */
public class SelectPicPopupWindow extends PopupWindow {

	/** 标题 */
	private TextView title;

	/**
	 * 拍照按钮
	 */
	private TextView btn_take_photo;
	/**
	 * 选择图片按钮
	 */
	private TextView btn_pick_photo;
	/**
	 * 取消按钮
	 */
	private TextView btn_cancel;
	private View mMenuView;

	public SelectPicPopupWindow(Context context, String tilte,
			OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwindow_headicon, null);

		title = (TextView) mMenuView
				.findViewById(R.id.popupwindow_headicon_title);
		btn_take_photo = (TextView) mMenuView
				.findViewById(R.id.popupwindow_headicon_btnCamera);
		btn_pick_photo = (TextView) mMenuView
				.findViewById(R.id.popupwindow_headicon_btnPhoto);
		btn_cancel = (TextView) mMenuView
				.findViewById(R.id.popupwindow_headicon_btnCancel);

		title.setText(tilte);

		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupwindowAnimationStyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}