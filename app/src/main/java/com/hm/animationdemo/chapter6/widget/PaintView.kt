package com.hm.animationdemo.chapter6.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by dumingwei on 2017/3/4.
 * 《Android自定义控件开发入门与实战》书中，第6章211页，关于FontMetrics 和 几条线之间的关系的公式我认为表达不合适。
 * 1. 在我们设置了Paint的textSize以后，FontMetrics的 ascent，descent，top，bottom就是已知的
 * 2. 在调用Canvas的drawText(String text, float x, float y, Paint paint)方法传入的y就是baseLine的y坐标
 * 所以有如下结论：
 *
 * ascent线的y坐标 = baseLine的y坐标 + FontMetrics.ascent
 * descent线的y坐标 = baseLine的y坐标 + FontMetrics.descent
 * top线的y坐标 = baseLine的y坐标 + FontMetrics.top
 * bottom线的y坐标 = baseLine的y坐标 + FontMetrics.bottom
 *
 *
 * tip 如果知道了文字所在的中间线，那么
 * baseLine线的y坐标=center-(FontMetrics.top+FontMetrics.bottom)/2
 *
 */
class PaintView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val TAG = "PaintView"
    private val rect = Rect()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

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
        testTextBaseLine(canvas)
        //testTextBaseLine1(canvas)
        //testTextBaseLine2(canvas)
    }

    private val myBlog = "leilifengxing's blog"

    private fun testTextBaseLine(canvas: Canvas) {
        val baseLineX = 0f
        val baseLineY = measuredHeight / 2f
        //mPaint.color = Color.RED

        mPaint.color = Color.GREEN
        mPaint.textAlign = Paint.Align.LEFT
        mPaint.textSize = 120f

        canvas.drawText(myBlog, baseLineX, baseLineY, mPaint)

        val fm = mPaint.fontMetrics

        val fmTop = fm.top
        val fmAscent = fm.ascent
        val fmDescent = fm.descent
        val fmBottom = fm.bottom
        Log.d(TAG, "testTextBaseLine: $fmTop,$fmAscent,$fmDescent,$fmBottom")

        val top = baseLineY + fmTop
        val ascent = baseLineY + fmAscent
        val descent = baseLineY + fmDescent
        val bottom = baseLineY + fmBottom

        mPaint.color = Color.RED
        //画基线
        canvas.drawLine(baseLineX, baseLineY, measuredWidth * 1.0f, baseLineY, mPaint)

        mPaint.color = Color.BLACK
        //画top
        canvas.drawLine(baseLineX, top, measuredWidth * 1.0f, top, mPaint)

        mPaint.color = Color.BLUE
        //画ascent
        canvas.drawLine(baseLineX, ascent, measuredWidth * 1.0f, ascent, mPaint)


        mPaint.color = Color.parseColor("#FF4081")
        //画descent
        canvas.drawLine(baseLineX, descent, measuredWidth * 1.0f, descent, mPaint)

        mPaint.color = Color.BLACK
        //画bottom
        canvas.drawLine(baseLineX, bottom, measuredWidth * 1.0f, bottom, mPaint)


        /**
         * 字符串所占区域的高度,和设置字体大小有关
         */
        val height = bottom - top
        Log.d(TAG, "testTextBaseLine: height=$height，measureHeight=$measuredHeight")

        //字符串所占区域的宽度
        val width = mPaint.measureText(myBlog)
        Log.d(TAG, "testTextBaseLine: width=$width")

        /**
         * 获取最小矩形
         */

        mPaint.getTextBounds(myBlog, 0, myBlog.length, rect)
        rect.top += baseLineY.toInt()
        rect.bottom += baseLineY.toInt()
        Log.d(TAG, "testTextBaseLine: ${rect.toShortString()}")
    }

    /**
     * 定点写字
     */
    private var givenTop = 200f

    private fun testTextBaseLine1(canvas: Canvas) {
        val baseLineX = 0f

        mPaint.color = Color.BLUE
        mPaint.textAlign = Paint.Align.LEFT
        mPaint.textSize = 120f

        //画出指定的top线
        canvas.drawLine(baseLineX, givenTop, measuredWidth.toFloat(), givenTop, mPaint)

        val fm = mPaint.fontMetrics

        val baseLineY = givenTop - fm.top

        mPaint.color = Color.RED
        canvas.drawLine(baseLineX, baseLineY, measuredWidth.toFloat(), baseLineY, mPaint)

        //画出指定的baseline线
        mPaint.color = Color.GREEN
        canvas.drawText(myBlog, baseLineX, baseLineY, mPaint)
    }

    private fun testTextBaseLine2(canvas: Canvas) {
        val baseLineX = 0f

        mPaint.color = Color.BLUE
        mPaint.textAlign = Paint.Align.LEFT
        mPaint.textSize = 120f

        //画出指定的top线
        canvas.drawLine(baseLineX, givenTop, measuredWidth.toFloat(), givenTop, mPaint)

        val fm = mPaint.fontMetrics

        val baseLineY = givenTop - fm.top

        mPaint.color = Color.RED
        canvas.drawLine(baseLineX, baseLineY, measuredWidth.toFloat(), baseLineY, mPaint)

        //画出指定的baseline线
        mPaint.color = Color.GREEN
        canvas.drawText(myBlog, baseLineX, baseLineY, mPaint)
    }


}
