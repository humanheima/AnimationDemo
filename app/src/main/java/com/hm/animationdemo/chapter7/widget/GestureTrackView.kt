package com.hm.animationdemo.chapter7.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by qijian on 17/1/13.
 */
class GestureTrackView(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val paint = Paint()
    private val path = Path()

    private var mPreX: Float = 0f
    private var mPreY: Float = 0f


    init {

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                mPreX = event.x
                mPreY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (mPreX + event.x) / 2
                val endY = (mPreY + event.y) / 2
                path.quadTo(mPreX, mPreY, endX, endY)
                mPreX = event.x
                mPreY = event.y
                invalidate()
            }
        }
        return super.onTouchEvent(event)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawPath(path, paint)
    }
}
