package com.hm.animationdemo.chapter7.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by qijian on 17/1/13.
 */
class AnimWaveView(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val paint = Paint()
    private val path = Path()

    private var mItemWaveLength = 1200

    /**
     * 波浪的偏移量
     */
    private var dx = 0

    init {
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL

        val anim = ValueAnimator.ofInt(0, mItemWaveLength)
        anim.duration = 2000
        anim.repeatCount = ValueAnimator.INFINITE
        anim.interpolator = LinearInterpolator()
        anim.addUpdateListener {
            dx = it.animatedValue as Int
            postInvalidate()
        }
        anim.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecModel = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecModel = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecModel == View.MeasureSpec.AT_MOST && heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE)
        } else if (widthSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightSpecSize)
        } else if (heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_SIZE)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        path.reset()
        val originY = 300f
        val halfWaveLen = mItemWaveLength / 2f
        path.moveTo((-mItemWaveLength + dx).toFloat(), originY)

        var i = -mItemWaveLength
        while (i <= width + mItemWaveLength) {
            path.rQuadTo(halfWaveLen / 2f, -200f, halfWaveLen, 0f)
            path.rQuadTo(halfWaveLen / 2, 200f, halfWaveLen, 0f)
            i += mItemWaveLength
        }
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }
}
