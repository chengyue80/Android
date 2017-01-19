package com.yue.demo.graphics;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.graphics.view.ShapeHolder;
import com.yue.demo.util.LogUtil;

/**
 * 小球下落
 * 
 * @author chengyue
 * 
 */
public class AnimatorTest extends Activity {
    // 定义小球的大小的常量
    static final float BALL_SIZE = 50F;
    // 定义小球从屏幕顶部、下落到屏幕底端的总时间
    static final float FULL_TIME = 1000;

    float startY;
    float endY;

    AnimatorSet bouncer;
    ValueAnimator fallAnim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        setContentView(ll);
        // 设置该窗口显示MyAnimationView组件
        ll.addView(new MyAnimationView(this));
    }

    public class MyAnimationView extends View implements AnimatorUpdateListener {
        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();

        public MyAnimationView(Context context) {
            super(context);
            // setBackgroundColor(Color.WHITE);

            ObjectAnimator colorAnimator = (ObjectAnimator) AnimatorInflater
                    .loadAnimator(context, R.anim.color_anim);

            colorAnimator.setEvaluator(new ArgbEvaluator());
            // 对该View本身应用属性动画
            colorAnimator.setTarget(this);
            colorAnimator.start();

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // // 如果触碰事件不是按下、移动事件
            // if (event.getAction() != MotionEvent.ACTION_DOWN
            // && event.getAction() != MotionEvent.ACTION_MOVE) {
            // return false;
            // }
            // // 在事件发生点添加一个小球（用一个圆形代表）
            // ShapeHolder newBall = addBall(event.getX(), event.getY());
            // // 计算小球下落动画开始时的y坐标
            // float startY = newBall.getY();
            // // 计算小球下落动画结束时的y坐标（落到屏幕最下方，就是屏幕高度减去小球高度）
            // float endY = getHeight() - BALL_SIZE;
            // // 计算动画的持续时间
            // int duration = (int) (FULL_TIME * ((getHeight() - event.getY()) /
            // getHeight()));
            // // 定义小球“落下”的动画：让newBall对象的y属性从事件发生点变化到屏幕最下方
            // /**
            // * 使用ObjectAnimator的ofInt()、ofFloat()、ofObject()
            // * 静态方法创建ObjectAnimator时，需要指定具体的对象和对象的属性名
            // */
            // ValueAnimator fallAnim = ObjectAnimator.ofFloat(newBall, "y",
            // startY, endY);
            // // 设置fallAnim动画的持续时间
            // fallAnim.setDuration(duration);
            // // 设置fallAnim动画的插值方式：加速插值
            // fallAnim.setInterpolator(new AccelerateInterpolator());
            // // 为fallAnim动画添加监听器：
            // // 当ValueAnimator的属性值发生改变时，将会激发该监听器的事件监听方法
            // fallAnim.addUpdateListener(this);
            // // 定义对newBall对象的alpha属性执行从1到0的动画（即定义渐隐动画）
            // ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(newBall,
            // "alpha",
            // 1f, 0f);
            // // 设置动画持续时间
            // fadeAnim.setDuration(duration);
            // // 为fadeAnim动画添加监听器
            // fadeAnim.addListener(new AnimatorListenerAdapter() {
            // // 当动画结束时
            // @Override
            // public void onAnimationEnd(Animator animation) {
            // // 动画结束时将该动画关联的ShapeHolder删除
            // balls.remove(((ObjectAnimator) animation).getTarget());
            // }
            // });
            // // 为fadeAnim动画添加监听器：
            // // 当ValueAnimator的属性值发生改变时，将会激发该监听器的事件监听方法
            // fadeAnim.addUpdateListener(this);
            // // 定义一个AnimatorSet来组合动画
            // AnimatorSet animatorSet = new AnimatorSet();
            // // 指定在播放fadeAnim之前，先播放fallAnim动画
            // animatorSet.play(fallAnim).before(fadeAnim);
            // // animatorSet.playTogether(fallAnim,fadeAnim);
            // // 开发播放动画
            // animatorSet.start();
            // return true;

            // 如果触碰事件不是按下、移动事件
            if (event.getAction() != MotionEvent.ACTION_DOWN
                    && event.getAction() != MotionEvent.ACTION_MOVE) {
                return false;
            }
            // 在事件发生点添加一个小球（用一个圆形代表）
            final ShapeHolder newBall = addBall(event.getX(), event.getY());
            // 计算小球下落动画开始时的y坐标
            startY = newBall.getY();
            // 计算小球下落动画结束时的y坐标（落到屏幕最下方，就是屏幕高度减去小球高度）
            endY = getHeight() - BALL_SIZE;

            LogUtil.d(MainActivity_Graphics.Tag + "1----startY : " + startY
                    + "   endY : " + endY);
            // 获取屏幕高度
            float h = (float) getHeight();
            float eventY = event.getY();
            // 计算动画的持续时间
            int duration = (int) (FULL_TIME * ((h - eventY) / h));
            // 定义小球“落下”的动画：让newBall对象的y属性从事件发生点变化到屏幕最下方
            fallAnim = ObjectAnimator.ofFloat(newBall, "y", startY, endY);
            // 设置fallAnim动画的持续时间
            fallAnim.setDuration(duration);
            // 设置fallAnim动画的插值方式：加速插值
            fallAnim.setInterpolator(new AccelerateInterpolator());

            /**
             * 定义小球“压扁”的动画：该动画控制小球的x坐标“向左移”半个球
             */
            ValueAnimator squashAnim1 = ObjectAnimator.ofFloat(newBall, "x",
                    newBall.getX(), newBall.getX() - BALL_SIZE / 2);
            // 设置squashAnim1动画持续时间
            squashAnim1.setDuration(duration / 4);
            // 设置squashAnim1动画的重复1次
            squashAnim1.setRepeatCount(1);
            // 设置squashAnim1动画的重复方式
            squashAnim1.setRepeatMode(ValueAnimator.REVERSE);
            // 设置squashAnim1动画的插值方式：减速插值
            squashAnim1.setInterpolator(new DecelerateInterpolator());

            /**
             * 定义小球“压扁”的动画：该动画控制小球的宽度加倍
             */
            ValueAnimator squashAnim2 = ObjectAnimator
                    .ofFloat(newBall, "width", newBall.getWidth(),
                            newBall.getWidth() + BALL_SIZE);
            // 设置squashAnim2动画持续时间
            squashAnim2.setDuration(duration / 4);
            // 设置squashAnim2动画的重复1次
            squashAnim2.setRepeatCount(1);
            // 设置squashAnim2动画的重复方式
            squashAnim2.setRepeatMode(ValueAnimator.REVERSE);
            // 设置squashAnim2动画的插值方式：减速插值
            squashAnim2.setInterpolator(new DecelerateInterpolator());

            /**
             * 定义小球“拉伸”的动画：该动画控制小球的y坐标“向下移”半个球
             */
            ObjectAnimator stretchAnim1 = ObjectAnimator.ofFloat(newBall, "y",
                    endY, endY + BALL_SIZE / 2);
            // 设置stretchAnim1动画持续时间
            stretchAnim1.setDuration(duration / 4);
            // 设置stretchAnim1动画重复1次
            stretchAnim1.setRepeatCount(1);
            // 设置stretchAnim1动画的重复方式
            stretchAnim1.setRepeatMode(ValueAnimator.REVERSE);
            // 设置stretchAnim1动画的插值方式：减速插值
            stretchAnim1.setInterpolator(new DecelerateInterpolator());
            /**
             * 定义小球“拉伸”的动画：该动画控制小球的高度减半
             */
            ValueAnimator stretchAnim2 = ObjectAnimator.ofFloat(newBall,
                    "height", newBall.getHeight(), newBall.getHeight()
                            - BALL_SIZE / 2);
            // 设置stretchAnim2动画持续时间
            stretchAnim2.setDuration(duration / 4);
            // 设置squashAnim2动画的重复1次
            stretchAnim2.setRepeatCount(1);
            // 设置squashAnim2动画的重复方式
            stretchAnim2.setRepeatMode(ValueAnimator.REVERSE);
            // 设置squashAnim2动画的插值方式：减速插值
            stretchAnim2.setInterpolator(new DecelerateInterpolator());
            /**
             * 定义小球“弹起”的动画
             */
            ObjectAnimator bounceBackAnim = ObjectAnimator.ofFloat(newBall,
                    "y", endY, startY + (getHeight() - startY) / 2);
            // 设置持续时间
            bounceBackAnim.setDuration(duration);
            // 设置动画的插值方式：减速插值
            bounceBackAnim.setInterpolator(new DecelerateInterpolator());
            // 使用AnimatorSet按顺序播放“掉落/压扁&拉伸/弹起动画
            bouncer = new AnimatorSet();
            // 定义在squashAnim1动画之前播放fallAnim下落动画
            bouncer.play(fallAnim).before(squashAnim1);
            // 由于因为小球在“屏幕”下方弹起时，小球要被被压扁
            // 即：宽度加倍、x坐标左移半个球，高度减半、y坐标下移半个球
            // 因此此处指定播放squashAnim1的同时，
            // 还播放squashAnim2、stretchAnim1、stretchAnim2
            bouncer.play(squashAnim1).with(squashAnim2);
            bouncer.play(squashAnim1).with(stretchAnim1);
            bouncer.play(squashAnim1).with(stretchAnim2);
            // 指定播放stretchAnim2动画之后，播放bounceBackAnim弹起动画
            bouncer.play(bounceBackAnim).after(stretchAnim2);

            startY = startY + (getHeight() - startY) / 2;

            // 定义对newBall对象的alpha属性执行从1到0的动画（即定义渐隐动画）
            final ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(newBall,
                    "alpha", 1f, 0f);
            // 设置动画持续时间
            fadeAnim.setDuration(250);
            // 为fadeAnim动画添加监听器
            fadeAnim.addListener(new AnimatorListenerAdapter() {
                // 当动画结束时
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束时将该动画关联的ShapeHolder删除
                    // balls.remove(((ObjectAnimator) animation).getTarget());
                    // fallAnim.setObjectValues(startY, endY);
                    AnimatorSet animatorSet1 = new AnimatorSet();
                    // 指定在播放fadeAnim之前，先播放bouncer动画
                    animatorSet1.play(bouncer).before(fadeAnim);
                    // 开发播放动画
                    animatorSet1.start();

                }
            });

            fallAnim.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    LogUtil.d(MainActivity_Graphics.Tag + "onAnimationStart");
                    LogUtil.d(MainActivity_Graphics.Tag + "startY : " + startY);
                    fallAnim = ObjectAnimator.ofFloat(newBall, "y", startY,
                            endY);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // TODO Auto-generated method stub
                    LogUtil.d(MainActivity_Graphics.Tag + "onAnimationRepeat");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    LogUtil.d(MainActivity_Graphics.Tag + "onAnimationEnd");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub
                    LogUtil.d(MainActivity_Graphics.Tag + "onAnimationCancel");
                }
            });
            // 再次定义一个AnimatorSet来组合动画
            AnimatorSet animatorSet = new AnimatorSet();
            // 指定在播放fadeAnim之前，先播放bouncer动画
            animatorSet.play(bouncer).before(fadeAnim);
            // 开发播放动画
            animatorSet.start();

            return true;

        }

        private ShapeHolder addBall(float x, float y) {
            // 创建一个圆
            OvalShape circle = new OvalShape();
            // 设置该椭圆的宽、高
            circle.resize(BALL_SIZE, BALL_SIZE);
            // 将圆包装成Drawable对象
            ShapeDrawable drawable = new ShapeDrawable(circle);
            // 创建一个ShapeHolder对象
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            // 设置ShapeHolder的x、y坐标
            shapeHolder.setX(x - BALL_SIZE / 2);
            shapeHolder.setY(y - BALL_SIZE / 2);
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            // 将red、green、blue3个随机数组合成ARGB颜色
            int color = 0xff000000 + red << 16 | green << 8 | blue;
            // 获取drawable上关联的画笔
            Paint paint = drawable.getPaint();
            // 将red、green、blue3个随机数除以4得到商值组合成ARGB颜色
            int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue
                    / 4;
            // 创建圆形渐变,圆心坐标，直径，渐变颜色，模式：延长边缘色
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
                    BALL_SIZE, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            // 为shapeHolder设置paint画笔
            shapeHolder.setPaint(paint);
            balls.add(shapeHolder);
            return shapeHolder;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // 遍历balls集合中的每个ShapeHolder对象
            for (ShapeHolder shapeHolder : balls) {
                // 保存canvas的当前坐标系统
                canvas.save();
                // 坐标变换：将画布坐标系统平移到shapeHolder的X、Y坐标处。
                canvas.translate(shapeHolder.getX(), shapeHolder.getY());
                // 将shapeHolder持有的圆形绘制在Canvas上
                shapeHolder.getShape().draw(canvas);
                // 恢复Canvas坐标系统
                canvas.restore();
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 指定重绘该界面。
            this.invalidate(); // ①
        }
    }
}