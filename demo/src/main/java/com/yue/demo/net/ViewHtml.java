package com.yue.demo.net;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.yue.demo.MainActivity;

/**
 * webview通过代码加载HTML
 * @author chengyue
 *
 */
public class ViewHtml extends Activity {

	private WebView webView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		StringBuilder sb = new StringBuilder();
		// 拼接一段HTML代码
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title> 欢迎您 </title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<h2> 欢迎您访问<a href=\"http://www.baidu.com\">"
				+ "百度网页</a></h2>");
		String content = "qqqqwqwertyuioiijgnvksnvohwonkvnoajowgniuvnnjkncmnochengyuwenjcmjcqoijcnnknnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu11111111111111111111111111111111111111111guuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu1111111111111111111111111111111111111";
		sb.append("<p style=\"word-break:break-all;\">"+content+"</p>");//自动换行
		sb.append("</body>");
		sb.append("</html>");
		// 使用简单的loadData方法会导致乱码，可能是Android API的Bug
		// show.loadData(sb.toString() , "text/html" , "utf-8");
		// 加载、并显示HTML代码
		webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8",
				null);

	}

	private void initView() {

		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(MainActivity.layoutParams_mm);
		layout.setOrientation(LinearLayout.VERTICAL);

		webView = new WebView(this);
		webView.setLayoutParams(MainActivity.layoutParams_ww);
		layout.addView(webView);

		setContentView(layout);
	}
}
