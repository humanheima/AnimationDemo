package com.hm.animationdemo.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hm.animationdemo.evaluator.ColorEvaluator;
import com.hm.animationdemo.evaluator.PointEvaluator;
import com.hm.animationdemo.model.MyPoint;

public class PointAnimView extends View {

    public static final float RADIUS = 50f;  //point点的半径

    private MyPoint currentPoint = new MyPoint(RADIUS, RADIUS);

    private Paint mPaint;
    private String color;

    public PointAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //第一次绘制初始的点之后就启动动画
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    /*private void startAnimation() {
        MyPoint startPoint = new MyPoint(RADIUS, RADIUS);
        MyPoint endPoint = new MyPoint(getWidth() - RADIUS, getHeight() - RADIUS);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (MyPoint) animation.getAnimatedValue();
                invalidate(); //会调用onDraw()方法 
            }
        });
        anim.setDuration(5000);
        anim.start();
    }*/

    public void startAnimation() {
        MyPoint startPoint = new MyPoint(RADIUS, RADIUS);
        MyPoint endPoint = new MyPoint(getWidth() - RADIUS, getHeight() - RADIUS);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(animation -> {
            currentPoint = (MyPoint) animation.getAnimatedValue();
            invalidate();
        });

        ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(),
                "#FF0000FF", "#FFFF0000");


        ValueAnimator argbAnim = ValueAnimator.ofInt(0xff0000ff, 0xffff0000);
        argbAnim.setEvaluator(new ArgbEvaluator());
        argbAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = ((int) animation.getAnimatedValue());
                mPaint.setColor(curValue);
            }
        });

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim).with(argbAnim);
        animSet.setDuration(5000);
        animSet.start();

    }

    //这两个方法是为了可以设置和获取color属性
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
    }
}