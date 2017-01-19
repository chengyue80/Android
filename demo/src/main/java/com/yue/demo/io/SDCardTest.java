package com.yue.demo.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.yue.demo.util.LogUtil;

/**
 * SDcard
 * 
 * @author chengyue
 * 
 */
public class SDCardTest extends Activity {
	final String FILE_NAME = "/file.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ll.setOrientation(LinearLayout.VERTICAL);
		// ll.setGravity(Gravity.CENTER);

		final EditText editText = new EditText(this);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		editText.setHint("read");
		ll.addView(editText);

		Button read = new Button(this);
		read.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		read.setText("读取");
		ll.addView(read);

		final EditText editText1 = new EditText(this);
		editText1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		editText1.setHint("write");
		ll.addView(editText1);

		Button write = new Button(this);
		write.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		write.setText("写入");
		ll.addView(write);

		setContentView(ll);
		// 为write按钮绑定事件监听器
		write.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				// 将edit1中的内容写入文件中
				write(editText1.getText().toString());
				editText1.setText("");
			}
		});

		read.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 读取指定文件中的内容，并显示出来
				editText.setText(read());
			}
		});
	}

	private String read() {
		try {

			Toast.makeText(this,
					"sd state : " + Environment.getExternalStorageState(), 2000)
					.show();
			LogUtil.d("File",
					"sd state : " + Environment.getExternalStorageState());
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡对应的存储目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				// 获取指定文件对应的输入流
				FileInputStream fis = new FileInputStream(
						sdCardDir.getCanonicalPath() + FILE_NAME);
				// 将指定输入流包装成BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				// 循环读取文件内容
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				// 关闭资源
				br.close();
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void write(String content) {
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				File targetFile = new File(sdCardDir.getCanonicalPath()
						+ FILE_NAME);
				// 以指定文件创建 RandomAccessFile对象
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				// 将文件记录指针移动到最后
				raf.seek(targetFile.length());
				// 输出文件内容
				raf.write(content.getBytes());
				// 关闭RandomAccessFile
				raf.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}