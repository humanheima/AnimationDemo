package com.hm.animationdemo.chapter7.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Created by qijian on 17/1/13.
 */
class WaveView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val paint = Paint()
    private val path = Path()

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

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.color = Color.BLACK
        paint.isAntiAlias = true

        /*path.moveTo(100f, 300f)
        path.quadTo(200f, 200f, 300f, 300f)
        path.quadTo(400f, 400f, 500f, 300f)*/

        path.moveTo(100f, 300f)
        path.rQuadTo(100f, -100f, 200f, 0f)
        path.rQuadTo(100f, 100f, 200f, 0f)

        canvas.drawPath(path, paint)
    }
}
