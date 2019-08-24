package com.hm.animationdemo.evaluator

import android.animation.TypeEvaluator
import android.graphics.Point

/**
 * Created by dumingwei on 2019-08-21.
 * Desc:
 */
class CircleViewEvaluator : TypeEvaluator<Point> {

    private var point: Point = Point()
    override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {

        point.x = (startValue.x + fraction * (endValue.x - startValue.x)).toInt()

        if (fraction * 2 < 1) {
            point.y = (startValue.y + fraction * 2 * (endValue.y - startValue.y)).toInt()
        } else {
            point.y = endValue.y
        }
        return point
    }

}