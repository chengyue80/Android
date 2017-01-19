package com.yue.demo.io;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;

/**
 * 自动朗读
 * 
 * @author chengyue
 * 
 */
public class Speech extends Activity {
    TextToSpeech tts;
    EditText editText;
    Button speech;
    Button record;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);

        editText = new EditText(this);
        editText.setLayoutParams(MainActivity.layoutParams_mw);
        editText.setLines(5);
        ll.addView(editText);

        speech = new Button(this);
        speech.setLayoutParams(MainActivity.layoutParams_ww);
        speech.setText("朗读");
        ll.addView(speech);

        record = new Button(this);
        record.setLayoutParams(MainActivity.layoutParams_ww);
        record.setText("记录声音");
        ll.addView(record);

        setContentView(ll);
        // 初始化TextToSpeech对象
        tts = new TextToSpeech(this, new OnInitListener() {
            @Override
            public void onInit(int status) {
                // 如果装载TTS引擎成功
                if (status == TextToSpeech.SUCCESS) {
                    // 设置使用美式英语朗读
                    int result = tts.setLanguage(Locale.US);
                    // 如果不支持所设置的语言
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE) {
                        Toast.makeText(Speech.this, "TTS暂时不支持这种语言的朗读。",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
        speech.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 执行朗读
                tts.speak(editText.getText().toString(),
                        TextToSpeech.QUEUE_ADD, null);
            }
        });
        record.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 将朗读文本的音频记录到指定文件
                tts.synthesizeToFile(editText.getText().toString(), null,
                        Environment.getExternalStorageDirectory().getPath()
                                + "/test/sound.wav");
                Toast.makeText(Speech.this, "声音记录成功！", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onDestroy() {
        // 关闭TextToSpeech对象
        if (tts != null) {
            tts.shutdown();
        }
    }
}