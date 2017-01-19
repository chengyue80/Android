package com.yue.demo.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class MusicBox extends Activity implements OnClickListener {

    static final String tag = MusicBox.class.getSimpleName();
    /**
     * 触发的事件,1 代表播放/暂停 按钮；2代表 停止按钮；3代表上一曲；4代表下一曲；5代表更新播放进度
     */
    static final String KEY_PLAYSTATUE = "playstatue";

    // 获取界面中显示歌曲标题、作者文本框
    TextView title, author, txtCurrentTime, txtTotalTime;
    // 播放/暂停、停止按钮
    ImageButton play, stop, next, pre;
    ActivityReceiver activityReceiver;
    public static final String CTL_ACTION = "org.yue.mytest.action.CTL_ACTION";
    public static final String UPDATE_ACTION = "org.yue.mytest.action.UPDATE_ACTION";
    public static final String KEY_POSITION = "position";
    private SeekBar mSeekBar;// 进度条
    /**
     * 定义音乐的播放状态，0x11代表没有播放；0x12代表正在播放；0x13代表暂停;0x14总时间;0x15当前时间
     */
    int status = 0x11;

    /**
     * 歌曲id
     */
    private int[] ids = { 0, 1, 2 };
    /**
     * 所有的歌曲名
     */
    private String[] titles = new String[] { "心愿", "约定", "美丽新世界" };
    /**
     * 所有的歌手名
     */
    private String[] singers = new String[] { "未知艺术家", "周蕙", "伍佰" };

    /**
     * 当前正在播放的音乐id
     */
    private int position;

    private Handler handler;

    /** ============================================== */

    /** 定义播放声音的MediaPlayer **/
    static MediaPlayer mPlayer;
    /** 定义系统的示波器 **/
    private Visualizer mVisualizer;
    /** 定义系统的均衡器 **/
    private Equalizer mEqualizer;
    /** 定义系统的重低音控制器 **/
    private BassBoost mBass;
    /** 定义系统的预设音场控制器 **/
    private PresetReverb mPresetReverb;
    private LinearLayout layout;
    private List<Short> reverbNames = new ArrayList<Short>();
    private List<String> reverbVals = new ArrayList<String>();
    private ActionBar actionBar;

    /** ============================================== */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(tag, "----MusicBox onCreate----");
        // 设置控制音乐声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.music_main);

        // 获取该Activity的ActionBar，
        // 只有当应用主题没有关闭ActionBar时，该代码才能返回ActionBar
        actionBar = getActionBar();
        // 设置是否显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(true);
        // 将应用程序图标设置为可点击的按钮
        actionBar.setHomeButtonEnabled(true);
        actionBar.hide();

        mPlayer = getPlayer();
        handler = new Handler();
        activityReceiver = new ActivityReceiver();

        play = (ImageButton) findViewById(R.id.music_main_play);
        stop = (ImageButton) findViewById(R.id.music_main_stop);
        next = (ImageButton) findViewById(R.id.music_main_playnext);
        pre = (ImageButton) findViewById(R.id.music_main_playpre);

        title = (TextView) findViewById(R.id.music_main_title);
        author = (TextView) findViewById(R.id.music_main_author);
        txtTotalTime = (TextView) findViewById(R.id.music_main_tv_totaltime);
        txtCurrentTime = (TextView) findViewById(R.id.music_main_tv_currenttime);

        mSeekBar = (SeekBar) findViewById(R.id.music_main_seekBar);
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {

                if (fromUser) {
                    LogUtil.d(tag, "fromUser : " + fromUser);
                    Intent intent = new Intent(CTL_ACTION);
                    intent.putExtra("progress", progress);
                    intent.putExtra(KEY_PLAYSTATUE, 5);
                    sendBroadcast(intent);
                }
            }
        });

        layout = (LinearLayout) findViewById(R.id.music_main_eq);

        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        pre.setOnClickListener(this);

        getIntentData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        registerReceiver(activityReceiver, filter);

        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(KEY_POSITION, position);
        intent.putExtra("ids", ids);
        // intent.p
        startService(intent);

        // 初始化示波器
        setupVisualizer();
        // 初始化均衡控制器
        setupEqualizer();
        // 初始化重低音控制器
        setupBassBoost();
        // 初始化预设音场控制器
        setupPresetReverb();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                LogUtil.i(tag, tag + "handler ");
                Intent intent = new Intent();
                intent.setAction(CTL_ACTION);
                intent.putExtra(KEY_PLAYSTATUE, 1);
                sendBroadcast(intent);
            }
        }, 500);

    }

    /**
     * 获取intent传过来的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        titles = intent.getStringArrayExtra("titles");// 歌名
        position = intent.getIntExtra("position", 0);// 位置
        ids = intent.getIntArrayExtra("ids");// 歌曲id
        singers = intent.getStringArrayExtra("singers");// 歌手名
    }

    // 自定义的BroadcastReceiver，负责监听从Service传回来的广播
    public class ActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(MusicBox.tag,
                    "ActivityReceiver  intent : " + Uri.decode(intent.toUri(0)));
            int updateStatus = intent.getIntExtra(MusicService.KEY_UPDATE, -1);
            int musicId = intent.getIntExtra(MusicService.KEY_CURRPLAYER, -1);
            if (musicId >= 0) {
                title.setText(titles[musicId]);
                author.setText(singers[musicId]);
            }
            // 更新图标状态
            if (updateStatus < 0) {
                Toast.makeText(context, "播放界面图标更新失败！", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            switch (updateStatus) {
            case 0x11:

                play.setImageResource(R.drawable.player_play);
                break;
            case 0x12:

                play.setImageResource(R.drawable.player_pause);
                break;
            case 0x13:
                play.setImageResource(R.drawable.player_play);

                break;
            case 0x14:
                int totalTime = intent.getIntExtra(MusicService.KEY_TOTALTIME,
                        -1);
                txtTotalTime.setText(format(totalTime));
                mSeekBar.setMax(totalTime);

                break;
            case 0x15:
                int currentTime = intent.getIntExtra(
                        MusicService.KEY_CURRENTTIME, -1);
                txtCurrentTime.setText(format(currentTime));

                mSeekBar.setProgress(currentTime);

                break;

            }
            status = updateStatus;

        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(CTL_ACTION);
        LogUtil.d(tag, "----onclick start----");
        switch (v.getId()) {
        // 按下播放/暂停按钮
        case R.id.music_main_play:
            intent.putExtra(KEY_PLAYSTATUE, 1);
            break;
        // 停止
        case R.id.music_main_stop:

            intent.putExtra(KEY_PLAYSTATUE, 2);
            break;
        // 下一曲
        case R.id.music_main_playpre:
            intent.putExtra(KEY_PLAYSTATUE, 3);

            break;
        // 上一曲
        case R.id.music_main_playnext:
            intent.putExtra(KEY_PLAYSTATUE, 4);

            break;

        }
        // 发送广播，将被Service组件中的BroadcastReceiver接收到
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(activityReceiver);
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra(KEY_PLAYSTATUE, 2);
        sendBroadcast(intent);
        super.onDestroy();
    }

    public static MediaPlayer getPlayer() {

        // Mlog.d(MusicBox.tag, "----getPlayer start----");
        if (mPlayer == null) {
            LogUtil.d(MusicBox.tag, "mPlayer is null==================");
            mPlayer = new MediaPlayer();
        }
        return mPlayer;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && getPlayer() != null) {
            // 释放所有对象
            mVisualizer.release();
            mEqualizer.release();
            mPresetReverb.release();
            mBass.release();
        }
    }

    public String format(int time) {
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        return String.format("%d:%02d", min, sec);
    }

    /**
     * 初始化示波器
     */
    private void setupVisualizer() {

        final MyVisualizerView musicView = new MyVisualizerView(this);
        int hight = (int) (120f * getResources().getDisplayMetrics().density);
        LogUtil.d(tag, "density : " + getResources().getDisplayMetrics().density);

        musicView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, hight));
        layout.addView(musicView);

        // 以MediaPlayer的AudioSessionId创建Visualizer
        // 相当于设置Visualizer负责显示该MediaPlayer的音频数据
        mVisualizer = new Visualizer(getPlayer().getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {

            @Override
            public void onWaveFormDataCapture(Visualizer visualizer,
                    byte[] waveform, int samplingRate) {
                // 用waveform波形数据更新mVisualizerView组件
                // Mlog.d(tag, "waveform[] : " + waveform);
                musicView.updateVisualizer(waveform);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft,
                    int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);

        mVisualizer.setEnabled(true);

    }

    /**
     * 初始化均衡控制器
     */
    private void setupEqualizer() {

        mEqualizer = new Equalizer(0, getPlayer().getAudioSessionId());
        // 启用均衡控制效果
        mEqualizer.setEnabled(true);

        TextView textView = new TextView(this);
        textView.setText("均衡器：");
        layout.addView(textView);

        // 获取均衡控制器支持最小值和最大值
        final short minEQLevel = mEqualizer.getBandLevelRange()[0];
        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
        // 获取均衡控制器支持的所有频率

        short bands = mEqualizer.getNumberOfBands();
        for (short i = 0; i < bands; i++) {
            TextView eqTextView = new TextView(this);
            // 显示频率
            eqTextView.setLayoutParams(MainActivity.layoutParams_mw);
            eqTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            eqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            eqTextView.setText(mEqualizer.getCenterFreq(i) / 1000 + " Hz");
            layout.addView(eqTextView);
            // 创建一个水平排列组件的LinearLayout

            LinearLayout tmpLayout = new LinearLayout(this);
            tmpLayout.setOrientation(LinearLayout.HORIZONTAL);
            // 显示均衡器最小值
            TextView minDbTv = new TextView(this);
            minDbTv.setLayoutParams(MainActivity.layoutParams_ww);
            minDbTv.setText(minEQLevel / 100 + " dB");

            // 显示均衡器最大值
            TextView maxDbTv = new TextView(this);
            maxDbTv.setLayoutParams(MainActivity.layoutParams_ww);
            maxDbTv.setText(maxEQLevel / 100 + " dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            // 定义SeekBar做为调整工具
            SeekBar bar = new SeekBar(this);
            bar.setThumb(getResources().getDrawable(R.drawable.thumb_f));
            bar.setLayoutParams(layoutParams);
            bar.setMax(maxEQLevel - minEQLevel);
            bar.setProgress(mEqualizer.getBandLevel(i));

            final short band = i;

            bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                        boolean fromUser) {
                    // 设置该频率的均衡值
                    mEqualizer.setBandLevel(band,
                            (short) (minEQLevel + progress));
                }
            });

            tmpLayout.addView(minDbTv);
            tmpLayout.addView(bar);
            tmpLayout.addView(maxDbTv);

            layout.addView(tmpLayout);

        }

    }

    /**
     * 初始化重低音控制器
     */
    private void setupBassBoost() {

        mBass = new BassBoost(0, getPlayer().getAudioSessionId());
        mBass.setEnabled(true);

        TextView bbTextView = new TextView(this);
        bbTextView.setText("重低音：");
        layout.addView(bbTextView);

        SeekBar bar = new SeekBar(this);
        bar.setThumb(getResources().getDrawable(R.drawable.thumb_f));
        // 重低音的范围为0～1000
        bar.setMax(1000);
        bar.setProgress(0);
        layout.addView(bar);

        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {

                mBass.setStrength((short) progress);
            }
        });

    }

    /**
     * 初始化预设音场控制器
     */
    private void setupPresetReverb() {

        mPresetReverb = new PresetReverb(0, getPlayer().getAudioSessionId());
        mPresetReverb.setEnabled(true);

        TextView prTitle = new TextView(this);
        prTitle.setText("音场: ");
        layout.addView(prTitle);

        // 获取系统支持的所有预设音场
        for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
            reverbNames.add(i);
            reverbVals.add(mEqualizer.getPresetName(i));
        }

        // 使用Spinner做为音场选择工具
        final Spinner spinner = new Spinner(this);
        layout.addView(spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, reverbVals));

        spinner.setContentDescription(reverbVals.get(0));
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mPresetReverb.setPreset(reverbNames.get(position));
                spinner.setContentDescription(reverbVals.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private static class MyVisualizerView extends View {
        // bytes数组保存了波形抽样点的值
        private byte[] bytes;
        private float[] points;
        private Paint paint = new Paint();
        private Rect rect = new Rect();
        private byte type = 0;

        public MyVisualizerView(Context context) {
            super(context);

            bytes = null;
            paint.setStrokeWidth(1f);
            paint.setAntiAlias(true);
            paint.setColor(Color.GREEN);
            paint.setStyle(Style.FILL);
        }

        public void updateVisualizer(byte[] ftt) {
            bytes = ftt;
            // 通知该组件重绘自己。
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            // 当用户触碰该组件时，切换波形类型
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            type++;

            if (type >= 3) {
                type = 0;
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (bytes == null)
                return;

            canvas.drawColor(Color.WHITE);
            // 使用rect对象记录该组件的宽度和高度
            rect.set(0, 0, getWidth(), getHeight());

            switch (type) {
            // -------绘制块状的波形图-------
            case 0:
                for (int i = 0; i < bytes.length - 1; i++) {
                    float left = getWidth() * i / (bytes.length - 1);
                    // 根据波形值计算该矩形的高度
                    float top = rect.height() - (byte) (bytes[i + 1] + 128)
                            * rect.height() / 128;
                    float right = left + 1;
                    float bottom = rect.height();

                    // Mlog.i(tag, "left: " + left + "  top: " + top +
                    // "  right: "
                    // + right + "  bottom: " + bottom);
                    canvas.drawRect(left, top, right, bottom, paint);
                }
                break;

            // -------绘制柱状的波形图（每隔18个抽样点绘制一个矩形）-------
            case 1:
                for (int i = 0; i < bytes.length - 1; i += 18) {
                    float left = rect.width() * i / (bytes.length - 1);
                    // 根据波形值计算该矩形的高度
                    float top = rect.height() - (byte) (bytes[i + 1] + 128)
                            * rect.height() / 128;
                    float right = left + 6;
                    float bottom = rect.height();
                    // Mlog.i(tag, "left: " + left + "  top: " + top +
                    // "  right: "
                    // + right + "  bottom: " + bottom);
                    canvas.drawRect(left, top, right, bottom, paint);
                }
                break;
            // -------绘制曲线波形图-------
            case 2:
                // 如果point数组还未初始化
                if (points == null || points.length < bytes.length * 4) {
                    points = new float[bytes.length * 4];
                }
                for (int i = 0; i < bytes.length - 1; i++) {
                    // 计算第i个点的x坐标
                    points[i * 4] = rect.width() * i / (bytes.length - 1);
                    // 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
                    points[i * 4 + 1] = (rect.height() / 2)
                            + ((byte) (bytes[i] + 128)) * 128
                            / (rect.height() / 2);
                    // 计算第i+1个点的x坐标
                    points[i * 4 + 2] = rect.width() * (i + 1)
                            / (bytes.length - 1);
                    // 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
                    points[i * 4 + 3] = (rect.height() / 2)
                            + ((byte) (bytes[i + 1] + 128)) * 128
                            / (rect.height() / 2);
                }
                // 绘制波形曲线
                canvas.drawLines(points, paint);
                break;
            }
        }

    }
}
