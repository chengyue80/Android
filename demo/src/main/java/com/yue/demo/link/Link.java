package com.yue.demo.link;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yue.demo.R;
import com.yue.demo.link.board.GameService;
import com.yue.demo.link.board.impl.GameServiceImpl;
import com.yue.demo.link.object.GameConf;
import com.yue.demo.link.object.LinkInfo;
import com.yue.demo.link.view.GameView;
import com.yue.demo.link.view.Piece;
import com.yue.demo.util.LogUtil;

public class Link extends Activity {

	protected static final String TAG = "Link";
	// 游戏配置对象
	private GameConf config;
	// 游戏业务逻辑接口
	private GameService gameService;
	// 游戏界面
	private GameView gameView;
	// 开始按钮
	private Button startButton;
	// 记录剩余时间的TextView
	private TextView timeTextView;
	// 失败后弹出的对话框
	private AlertDialog.Builder lostDialog;
	// 游戏胜利后的对话框
	private AlertDialog.Builder successDialog;
	// 定时器
	private Timer timer = new Timer();
	// 记录游戏的剩余时间
	private int gameTime;
	// 记录是否处于游戏状态
	private boolean isPlaying = false;
	// 播放音效的SoundPool
	SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 8);
	int dis;
	// 记录已经选中的方块
	private Piece selected = null;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0x123:
				// Mlog.d(TAG, "handleMessage 0x123");
				timeTextView.setText("剩余时间： " + gameTime);
				gameTime--;
				if (gameTime < 0) {
					stopTimer();
					isPlaying = false;
					lostDialog.show();
					return;
				}
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.link);
		init();
		
		
	}

	/**
	 * 初始化游戏界面
	 */
	private void init() {
		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		LogUtil.d(TAG, "density : " + metrics.density);
		config = new GameConf(8, 9, 2, 10,metrics.density, this);
		// 得到游戏区域对象gameview
		gameView = (GameView) findViewById(R.id.gameView);
		// 获取显示剩余时间的文本框
		timeTextView = (TextView) findViewById(R.id.timeText);
		// 获取开始按钮
		startButton = (Button) findViewById(R.id.startButton);
		// 初始化音效

		dis = soundPool.load(this, R.raw.dis, 1);

		gameService = new GameServiceImpl(this.config);
		gameView.setGameService(gameService);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogUtil.d(TAG, "---> startButton onClick");

				startGame(GameConf.DEFAULT_TIME);
			}
		});

		gameView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				LogUtil.d(TAG, "gameView onTouch : " + event.getAction());
				if (!isPlaying) {

					return false;
				}

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					LogUtil.i(TAG, "gameView w/h: " + gameView.getWidth() + "/"
							+ gameView.getHeight());
					LogUtil.i(TAG, "event.getX() : " + event.getX()
							+ "  event.getY()" + event.getY());
					Piece[][] pieces = gameService.getPieces();
					// 获取点击的位置
					float touchX = event.getX();
					float touchY = event.getY();
					// 获取点击得到的piece
					Piece piece = gameService.findPiece(touchX, touchY);

					if (piece == null)
						return true;
					gameView.setSelectedPiece(piece);
					LogUtil.i(TAG, "pieces[i][j] : " + piece.getBeginX() + "  "
							+ piece.getBeginY());
					LogUtil.i(TAG, "piece width : " + piece.getImage().getImage().getWidth());
					// 之前没有选中任何一个Piece
					if (selected == null) {
						selected = piece;
						gameView.postInvalidate();
					} else {
						LinkInfo linkInfo = gameService.link(selected, piece);
						// 两个piece不可连
						if (linkInfo == null) {
							LogUtil.d(TAG, "linkInfo is null");
							selected = piece;
							gameView.postInvalidate();
						} else {
							LogUtil.d(TAG, "link success...");
							// 处理成功连接
							gameView.setLinkInfo(linkInfo);
							gameView.setSelectedPiece(null);
							gameView.postInvalidate();
							// 将两个Piece对象从数组中删除
							pieces[selected.getIndexX()][selected.getIndexY()] = null;
							pieces[piece.getIndexX()][piece.getIndexY()] = null;

							selected = null;

							soundPool.play(dis, 1, 1, 0, 0, 1);
							// 判断是否还有剩下的方块, 如果没有, 游戏胜利
							if (!gameService.hasPieces()) {
								successDialog.show();
								stopTimer();
								isPlaying = false;
							}
						}
					}

				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					gameView.postInvalidate();
				}
				return true;
			}
		});

		// 初始化游戏失败的对话框
		lostDialog = createDialog("Lost", "游戏失败！ 重新开始", R.drawable.lost)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);
					}
				});
		// 初始化游戏胜利的对话框
		successDialog = createDialog("Success", "游戏胜利！ 重新开始",
				R.drawable.success).setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);
					}
				});
	}

	/**
	 * 创建对话框的工具方法
	 * 
	 * @param title
	 * @param message
	 * @param imageResource
	 * @return
	 */
	private AlertDialog.Builder createDialog(String title, String message,
			int imageResource) {
		return new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(imageResource);
	}

	/**
	 * 以gameTime作为剩余时间开始或恢复游戏
	 * 
	 * @param gameTime
	 */
	private void startGame(int gameTime) {
		// 如果之前的timer还未取消，取消timer
		if (this.timer != null) {
			stopTimer();
		}
		// 重新设置游戏时间
		this.gameTime = gameTime;
		// 如果游戏剩余时间与总游戏时间相等，即为重新开始新游戏
		if (gameTime == GameConf.DEFAULT_TIME) {
			// 开始新的游戏游戏
			gameView.startGame();
		}
		isPlaying = true;
		this.timer = new Timer();
		// 启动计时器 ， 每隔1秒发送一次消息
		this.timer.schedule(new TimerTask() {
			public void run() {
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 1000);
		// 将选中方块设为null。
		this.selected = null;
	}

	private void stopTimer() {
		// 停止定时器
		handler.removeMessages(0x123);
		this.timer.cancel();
		this.timer = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopTimer();
	}

}
