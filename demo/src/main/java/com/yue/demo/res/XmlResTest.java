package com.yue.demo.res;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 解析xml原始资源
 * 
 * @author chengyue
 * 
 */
public class XmlResTest extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        Button bn = new Button(this);
        bn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        bn.setText("解析");
        ll.addView(bn);

        final TextView show = new TextView(this);
        show.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        show.setText("show");
        ll.addView(show);

        EditText editText = new EditText(this);
        editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        editText.setText("样式1的格式");
        editText.setTextAppearance(this, R.style.style1);
        ll.addView(editText);

        EditText editText1 = new EditText(this);
        editText1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        editText1.setText("样式2的格式");
        editText1.setTextAppearance(this, R.style.style2);
        ll.addView(editText1);

        setTheme(R.style.CrazyTheme);
        setContentView(ll);
        // 获取bn按钮，并为该按钮绑定事件监听器
        bn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 根据XML资源的ID获取解析该资源的解析器。
                // XmlResourceParser是XmlPullParser的子类。

                XmlResourceParser xrp = getResources().getXml(R.xml.books);
                try {
                    StringBuilder sb = new StringBuilder("");
                    // 还没有到XML文档的结尾处
                    while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                        // 如果遇到了开始标签

                        LogUtil.d(MainActivity_Res.Tag + "type : "
                                + xrp.getEventType());
                        if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                            // 获取该标签的标签名
                            String tagName = xrp.getName();

                            LogUtil.d(MainActivity_Res.Tag + "tagName = "
                                    + tagName);
                            // 如果遇到book标签
                            if (tagName.equals("book")) {
                                // Mlog.d(MainActivity_Res.Tag +
                                // "----book-----");
                                // 根据属性名来获取属性值
                                String bookPrice = xrp.getAttributeValue(null,
                                        "price");
                                sb.append("价格：" + bookPrice + "\n");
                                String publishdate = xrp.getAttributeValue(
                                        null, "publishdate");
                                sb.append("出版日期：" + publishdate + "\n");
                                // 根据属性索引来获取属性值
                                String bookName = xrp.getAttributeValue(2);
                                sb.append("书名：" + bookName + "\n");
                                // sb.append("信息：" + xrp.nextText() + "\n");
                            }
                            // 获取文本节点的值
                            else if (tagName.equals("introduction")) {
                                // Mlog.d(MainActivity_Res.Tag +
                                // "-----introduction----");
                                sb.append("信息：" + xrp.nextText() + "\n");
                                sb.append("==============\n");
                            }
                        } else if (xrp.getEventType() == XmlResourceParser.TEXT) {
                            sb.append("TEXT：" + xrp.getText() + "\n");
                            sb.append("==============\n");
                        }

                        // 获取解析器的下一个事件
                        xrp.next(); // ①
                    }
                    LogUtil.d(MainActivity_Res.Tag + "\nsb : " + sb.toString());
                    show.setText(sb.toString());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}