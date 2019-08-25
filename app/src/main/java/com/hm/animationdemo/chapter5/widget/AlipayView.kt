package com.hm.animationdemo.chapter5.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by dumingwei on 2019-08-24.
 * Desc: 支付宝支付成功的动画
 */
class AlipayView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private var mCirclePath: Path
    private var mDstPath: Path
    private var mPaint: Paint = Paint()

    private var mPathMeasure: PathMeasure

    private var mCurAnimValue: Float = 0f

    private val mCenterX = 100f
    private val mCenterY = 100f

    private val mRadius = 50f

    //标记是否有下一条路径
    private var mNext = false

    init {

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 4f
        mPaint.color = Color.BLACK

        mDstPath = Path()

        mCirclePath = Path()

        mCirclePath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW)

        //移动到对号的左边的起点
        mCirclePath.moveTo(mCenterX - mRadius / 2, mCenterY)
        mCirclePath.lineTo(mCenterX, mCenterY + mRadius / 2)
        mCirclePath.lineTo(mCenterX + mRadius / 2, mCenterY - mRadius / 3)

        mPathMeasure = PathMeasure(mCirclePath, false)

        val animator = ValueAnimator.ofFloat(0f, 2f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener {
            mCurAnimValue = it.animatedValue as Float

            invalidate()
        }

        animator.duration = 4000
        animator.start()
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

        if (mCurAnimValue < 1) {
            val stop: Float = mPathMeasure.length * mCurAnimValue
            mPathMeasure.getSegment(0f, stop, mDstPath, true)
        } else {
            if (!mNext) {
                mNext = true
                mPathMeasure.getSegment(0f, mPathMeasure.length, mDstPath, true)
                mPathMeasure.nextContour()
            }
            val stop: Float = mPathMeasure.length * (mCurAnimValue - 1)
            mPathMeasure.getSegment(0f, stop, mDstPath, true)
        }

        canvas.drawPath(mDstPath, mPaint)
    }


}