package com.hm.animationdemo.evaluator

import android.animation.TypeEvaluator

/**
 * Created by dumingwei on 2019-08-20.
 * Desc:
 */
class CharEvaluator : TypeEvaluator<Char> {


    override fun evaluate(fraction: Float, startValue: Char, endValue: Char): Char {
        val startInt = startValue.toInt()
        val endInt = endValue.toInt()
        val curInt = (startInt + fraction * (endInt - startInt)).toInt()
        return curInt.toChar()
    }

}