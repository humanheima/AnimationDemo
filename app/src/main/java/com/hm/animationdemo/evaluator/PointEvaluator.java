package com.hm.animationdemo.evaluator;

import android.animation.TypeEvaluator;

import com.hm.animationdemo.model.MyPoint;

public class PointEvaluator implements TypeEvaluator<MyPoint> {

    @Override
    public MyPoint evaluate(float fraction, MyPoint startValue, MyPoint endValue) {
    //改变x值
        float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
        //改变y值
        float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
        MyPoint point = new MyPoint(x, y);
        //返回一个新的Point对象，坐标已经改变了
        return point;
    }
}