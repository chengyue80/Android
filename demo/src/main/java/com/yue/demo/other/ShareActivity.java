package com.yue.demo.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iflytek.android.framework.annotation.ViewInject;
import com.yue.demo.R;
import com.yue.demo.RootActivity;
import com.yue.demo.util.LogUtil;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class ShareActivity extends RootActivity {

	private final String TAG = ShareActivity.class.getSimpleName();
	@ViewInject(id = R.id.button1, listenerName = "onClick", methodName = "onClick")
	private Button share;
	@ViewInject(id = R.id.button2, listenerName = "onClick", methodName = "onClick")
	private Button sina;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		share.setText("分享");
		sina.setText("新浪微博");
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			String path = Environment.getExternalStorageDirectory()
					+ "/DCIM/Camera/IMG_20150906_092239.jpg";
			LogUtil.d(TAG, "path : " + path);
			File file = new File(path);
			if (file.exists()) {
				LogUtil.d(TAG, "asset file is exists");
			} else {
				LogUtil.d(TAG, "asset file is not exists");

			}
			showShare();
			break;

		case R.id.button2:
			Platform sina = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			sina.setPlatformActionListener(new PlatformActionListener() {

				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					// TODO Auto-generated method stub
					LogUtil.d(TAG, "----onError----");

				}

				@Override
				public void onComplete(Platform arg0, int arg1,
						HashMap<String, Object> arg2) {
					// TODO Auto-generated method stub
					LogUtil.d(TAG, "----onComplete----");

				}

				@Override
				public void onCancel(Platform arg0, int arg1) {
					// TODO Auto-generated method stub
					LogUtil.d(TAG, "----onCancel----");

				}
			});
			sina.authorize();
			// 移除授权
			// sina.removeAccount();

			break;

		default:
			break;
		}
	}

	private void showShare() {
		// 在application中声明
		// ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("AndroidDemo分享");

		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(Environment.getExternalStorageDirectory()
				+ "/DCIM/Camera/IMG_20150906_092239.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

	/**
	 * 演示调用ShareSDK执行分享
	 * 
	 * @param context
	 * @param platformToShare
	 *            指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
	 * @param showContentEdit
	 *            是否显示编辑页
	 */
	public static void showShare(Context context, String platformToShare,
			boolean showContentEdit) {
		OnekeyShare oks = new OnekeyShare();
		oks.setSilent(!showContentEdit);
		if (platformToShare != null) {
			oks.setPlatform(platformToShare);
		}
		// ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC 第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		// oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
		oks.setTitle("ShareSDK--Title");
		oks.setTitleUrl("http://mob.com");
		oks.setText("ShareSDK--文本");
		// oks.setImagePath("/sdcard/test-pic.jpg"); //分享sdcard目录下的图片
		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		oks.setUrl("http://www.mob.com"); // 微信不绕过审核分享链接
		// oks.setFilePath("/sdcard/test-pic.jpg");
		// //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
		oks.setComment("分享"); // 我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
		oks.setSite("ShareSDK"); // QZone分享完之后返回应用时提示框上显示的名称
		oks.setSiteUrl("http://mob.com");// QZone分享参数
		oks.setVenueName("ShareSDK");
		oks.setVenueDescription("This is a beautiful place!");
		oks.setShareFromQQAuthSupport(false);
		// 将快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());
		// 去自定义不同平台的字段内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());
		// 在九宫格设置自定义的图标
		Bitmap enableLogo = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher);
		Bitmap disableLogo = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher);
		String label = "ShareSDK";
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {

			}
		};
		oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

		// 为EditPage设置一个背景的View
		// oks.setEditPageBackground(getPage());
		// 隐藏九宫格中的新浪微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);

		// String[] AVATARS = {
		// "http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		// "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		// "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		// "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		// "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		// "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
		// };
		// oks.setImageArray(AVATARS); //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

		// 启动分享
		oks.show(context);
	}

}
