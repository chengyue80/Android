package com.yue.demo.link.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.yue.demo.link.board.GameService;
import com.yue.demo.link.object.LinkInfo;
import com.yue.demo.link.util.ImageUtil;
import com.yue.demo.util.LogUtil;

public class GameView extends View {

	private final String tag = "GameView";
	// 游戏逻辑的实现类
	private GameService gameService;
	// 连接信息对象
	private LinkInfo linkInfo;

	// 保存当前被选中的方块
	private Piece selectedPiece;

	private Paint paint;

	// 选中标识的图片对象

	private Bitmap selectImage;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint = new Paint();
		// paint.setShader(new BitmapShader(BitmapFactory.decodeResource(
		// context.getResources(), R.drawable.heart),
		// Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
		paint.setColor(Color.RED);
		paint.setStrokeWidth(9);

		selectImage = ImageUtil.getSelectImage(context);

	}

	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	public void setLinkInfo(LinkInfo linkInfo) {
		this.linkInfo = linkInfo;
	}

	public void setSelectedPiece(Piece selectedPiece) {
		this.selectedPiece = selectedPiece;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		LogUtil.d(tag, "onDraw");
		if (gameService == null) {
			LogUtil.d(tag, "gameService = null");
			return;
		}
		Piece[][] pieces = gameService.getPieces();
		if (pieces != null) {
			for (int i = 0; i < pieces.length; i++) {

				for (int j = 0; j < pieces[i].length; j++) {
					// 如果二维数组中该元素不为空（即有方块），将这个方块的图片画出来
					if (pieces[i][j] != null) {
						Piece piece = pieces[i][j];
						canvas.drawBitmap(piece.getImage().getImage(),
								piece.getBeginX(), piece.getBeginY(), null);
					}
				}
			}
			Piece piece = pieces[pieces.length - 1][pieces[0].length - 1];
			if (piece != null)
				LogUtil.d(tag, "pieces[i][j] : " + piece.getBeginX() + "  "
						+ piece.getBeginY());
		}
		// 画选中标识的图片
		if (this.selectedPiece != null) {
			canvas.drawBitmap(this.selectImage, this.selectedPiece.getBeginX(),
					this.selectedPiece.getBeginY(), null);
		}
		// 如果当前对象中有linkInfo对象, 即连接信息
		if (this.linkInfo != null) {
			// 获取LinkInfo中封装的所有连接点
			List<Point> points = linkInfo.getLinkPoints();
			// 依次遍历linkInfo中的每个连接点
			for (int i = 0; i < points.size() - 1; i++) {
				// 获取当前连接点与下一个连接点
				Point currentPoint = points.get(i);
				Point nextPoint = points.get(i + 1);
				// 绘制连线
				canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x,
						nextPoint.y, this.paint);
			}
			this.linkInfo = null;
		}

	}

	public void startGame() {
		LogUtil.d(tag, "startGame");
		gameService.start();
		postInvalidate();
	}
}
