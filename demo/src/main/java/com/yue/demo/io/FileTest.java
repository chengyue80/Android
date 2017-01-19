package com.yue.demo.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yue.demo.util.LogUtil;

/**
 * openFileOutput 和 openFileInput
 * 
 * @author chengyue
 * 
 */
public class FileTest extends Activity {

    private static String FILE_NAME = "file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        final EditText editText1 = new EditText(this);
        editText1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        editText1.setHint("write");
        ll.addView(editText1);

        Button read = new Button(this);
        read.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        read.setText("读取");
        ll.addView(read);

        Button write = new Button(this);
        write.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        write.setText("写入");
        ll.addView(write);

        Button info = new Button(this);
        info.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        info.setText("info");
        ll.addView(info);

        final TextView textView = new TextView(this);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        ll.addView(textView);

        setContentView(ll);

        read.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                editText.setText(read());
            }
        });

        write.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                write(editText1.getText().toString());
            }
        });

        info.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String text = "";
                // 获取或创建FILE_NAME 对应的子目录
                File dir = getDir(FILE_NAME, MODE_APPEND);
                if (dir.exists()) {
                    LogUtil.d(MainActivity_IO.Tag + "files path : "
                            + dir.getPath());
                    text = text + "files path : " + dir.getPath() + "\n";
                }
                // 获取数据文件夹的绝对路径
                File ab = getFilesDir();
                if (ab.exists()) {
                    LogUtil.d(MainActivity_IO.Tag + "files absolute path : "
                            + ab.getPath());
                    text = text + "files absolute path : " + ab.getPath()
                            + "\n";
                }

                String[] files = fileList();
                for (String string : files) {

                    LogUtil.d(MainActivity_IO.Tag + "files name : " + string);
                    text = text + "files name : " + string + "\n";
                }

                textView.setText(text);
                deleteFile(FILE_NAME);

            }
        });
    }

    private String read() {
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput(FILE_NAME);
            byte[] bs = new byte[1024];
            int len = 0;
            StringBuilder builder = new StringBuilder("");

            while ((len = inputStream.read(bs)) > 0) {
                builder.append(new String(bs, 0, len));
            }
            inputStream.close();
            return builder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void write(String content) {
        FileOutputStream outputStream = null;
        PrintStream printStream = null;
        try {
            outputStream = openFileOutput(FILE_NAME, MODE_APPEND);

            printStream = new PrintStream(outputStream);
            printStream.print(content);
            printStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (printStream != null)
                    printStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
