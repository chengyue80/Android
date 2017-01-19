package com.iflytek.android.framework.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.iflytek.android.framework.util.ClassUtils;

/**
 * 自定义对话框
 * @author wzgao
 *
 */
public class BaseDialog {

	/**
	 * 
	 * 描述：通用Dialog
	 * @param context 上下文
	 * @param iconId 图标资源ID
	 * @param title 标题
	 * @param message 提示消息
	 * @param buttonName 按钮名称（数组）
	 * @throws
	 */
	public static void ShowDialog(Context context, int iconId, String title,
			String message, String[] buttonName, final String[] methodName, final Class<?> cl) {
		AlertDialog.Builder build = new AlertDialog.Builder(context);
		build.setIcon(iconId);
		build.setTitle(title);
		build.setMessage(message);
		build.setPositiveButton(buttonName[0],
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ClassUtils.invokeClickMethod(cl, methodName[0],
								ClassUtils.getClass(DialogInterface.class, int.class), dialog, which);
					}
				});
		build.setNegativeButton(buttonName[1],
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ClassUtils.invokeClickMethod(cl, methodName[1],
								ClassUtils.getClass(DialogInterface.class, int.class), dialog, which);
					}
				});
		build.show();
	}

}
