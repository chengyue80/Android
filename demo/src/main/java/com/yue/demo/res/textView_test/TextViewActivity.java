package com.yue.demo.res.textView_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.yue.demo.R;

public class TextViewActivity extends Activity {

	TextView mTextView;

	private String test = "上海市今天是多云,温度11~17℃";
	private Context mContext = TextViewActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_textview_test);

		mTextView = (TextView) findViewById(R.id.tv_textView_test);
		// mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		mTextView.setText(convertString(ToDBC(test)));
		// mTextView.setText(caculateChangeLine(test));

		((TextView) findViewById(R.id.tv_textView_test2))
				.setMovementMethod(ScrollingMovementMethod.getInstance());
		((TextView) findViewById(R.id.tv_textView_test2)).setText(test);
		Log.i("TAG", "str.length():" + "上".getBytes().length);
		Log.i("TAG", "str.length():" + "Chow".getBytes().length);
		Log.i("TAG", "str.length():" + "9".getBytes().length);
		Log.i("TAG", "str.length():" + "nn".getBytes().length);
		Log.i("TAG", "str.length():" + ",".getBytes().length);
		// Log.i("TAG", "str.length():" + convertString(test));

		// this.setContentView(R.layout.main);
		//
		// CYTextView mCYTextView;
		// mCYTextView = (CYTextView) findViewById(R.id.mv);
		// mCYTextView.SetText(test);
		System.out.println("text size : " + mTextView.getTextSize());
		System.out.println("textView width : " + mTextView.getWidth());

	}

	private int mHeight = 0;

	private String convertString(String str) {

		String returnStr = "";
		String s = "";
		TextPaint mPaint = mTextView.getPaint();
		// float size = mTextView.getTextSize();
		int width = dp2px(mContext, 285);
		// int height = XiriUtil.dp2px(mContext, 25);
		str.replace("\n", "");
		for (int i = 0; i < str.length(); i++) {
			s += str.substring(i, i + 1);
			if (mPaint.measureText(s) >= width) {
				// if (str.charAt(i) >= 'a' && str.charAt(i) <= 'z'
				// || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {
				// if (str.charAt(i - 1) >= 'a'
				// && str.charAt(i - 1) <= 'z'
				// || (str.charAt(i - 1) >= 'A' && str.charAt(i - 1) <= 'Z'))
				// returnStr += "-";
				// } else {
				// }
				returnStr += "\n";
				s = str.substring(i, i + 1);
				System.out.println(mPaint.measureText(str.substring(i, i + 1)));
			}
			returnStr += str.substring(i, i + 1);
		}

		return returnStr;

	}

	public static int dp2px(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return (int) ((dp * displayMetrics.density) + 0.5);
	}

	private String caculateOneLine(String str) {
		// 锟斤拷一锟斤拷没锟斤拷\n锟斤拷锟斤拷锟街斤拷锟叫伙拷锟斤拷
		String returnStr = "";
		TextPaint mPaint = mTextView.getPaint();
		int width = dp2px(mContext, 280);
		int height = dp2px(mContext, 25);
		int strWidth = (int) mPaint.measureText(str);
		int len = str.length();
		for (int i = 0; i < str.length(); i++) {
		}
		int lineNum = strWidth / width; // 锟斤拷锟街拷锟斤拷侄锟斤拷锟斤拷锟�
		int oneLine;
		int tempWidth = 0;
		String lineStr;
		int returnInt = 0;
		if (lineNum == 0) {
			returnStr = str;
			mHeight += height;
			return returnStr;
		} else {
			oneLine = len / (lineNum + 1); // 一锟叫达拷锟斤拷卸锟斤拷俑锟斤拷锟�
			lineStr = str.substring(0, oneLine);
			tempWidth = (int) mPaint.measureText(lineStr);
			if (tempWidth < width) // 锟斤拷锟叫★拷锟�锟揭碉拷锟斤拷锟斤拷歉锟�
			{
				while (tempWidth < width) {
					oneLine++;
					lineStr = str.substring(0, oneLine);
					tempWidth = (int) mPaint.measureText(lineStr);
				}
				returnInt = oneLine - 1;
				returnStr = lineStr.substring(0, lineStr.length() - 2);
			} else {
				while (tempWidth > width) {
					oneLine--;
					lineStr = str.substring(0, oneLine);
					tempWidth = (int) mPaint.measureText(lineStr);
				}
				returnStr = lineStr.substring(0, lineStr.length() - 1);
				returnInt = oneLine;
			}
			mHeight += height;
			returnStr += "\n" + caculateOneLine(str.substring(returnInt - 1));
		}
		return returnStr;
	}

	// 全锟角空革拷为12288锟斤拷锟斤拷强崭锟轿�2
	// 锟斤拷锟斤拷锟街凤拷锟斤拷(33-126)锟斤拷全锟斤拷(65281-65374)锟侥讹拷应锟斤拷系锟角ｏ拷锟斤拷锟斤拷锟�5248
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {

				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public String caculateChangeLine(String text) {
		StringBuilder newText = new StringBuilder();
		// text.replace("\n", "");
		String tempStr[] = text.toString().split("\n");
		int len = tempStr.length;
		for (int i = 0; i < len; i++) {
			newText.append(caculateOneLine(tempStr[i]));
			if (i != len - 1)
				newText.append("\n");
		}
		return newText.toString();
	}

	/**
	 * 锟皆讹拷锟街革拷锟侥憋拷
	 * 
	 * @param content
	 *            锟斤拷要锟街革拷锟斤拷谋锟�
	 * @param p
	 *            锟斤拷锟绞ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥憋拷锟侥匡拷锟�
	 * @param width
	 *            锟斤拷锟侥匡拷锟斤拷示锟斤拷锟截ｏ拷一锟斤拷为锟截硷拷锟侥匡拷龋锟�
	 * @return 一锟斤拷锟街凤拷锟斤拷锟介，锟斤拷锟斤拷每锟叫碉拷锟侥憋拷
	 */
	private String[] autoSplit(String content, Paint p, float width) {
		int length = content.length();
		float textWidth = p.measureText(content);
		if (textWidth <= width) {
			return new String[] { content };
		}

		int start = 0, end = 1, i = 0;
		int lines = (int) Math.ceil(textWidth / width); // 锟斤拷锟斤拷锟斤拷锟斤拷
		String[] lineTexts = new String[lines];
		while (start < length) {
			if (p.measureText(content, start, end) > width) { // 锟侥憋拷锟斤拷瘸锟斤拷锟斤拷丶锟斤拷锟斤拷时
				lineTexts[i++] = (String) content.subSequence(start, end);
				start = end;
			}
			if (end == length) { // 锟斤拷锟斤拷一锟叫碉拷锟侥憋拷
				lineTexts[i] = (String) content.subSequence(start, end);
				break;
			}
			end += 1;
		}
		return lineTexts;
	}

}
