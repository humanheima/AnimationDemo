package com.hm.animationdemo.chapter5.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.animationdemo.R

/**
 * Created by dumingwei on 2019-08-24.
 * Desc:
 */
class GetPosTanView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private var mCirclePath: Path? = null
    private var mDstPath: Path
    private var mPaint: Paint = Paint()

    private var mPathMeasure: PathMeasure

    private var mCurAnimValue: Float = 0f

    private var bitmap: Bitmap
    private val pos = FloatArray(2)
    private val tan = FloatArray(2)
    private val mMatrix: Matrix = Matrix()            // 矩阵,用于对图片进行一些操作

    init {

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 4f
        mPaint.color = Color.BLACK

        mDstPath = Path()

        mCirclePath = Path()

        mCirclePath?.addCircle(100f, 100f, 50f, Path.Direction.CW)

        mPathMeasure = PathMeasure(mCirclePath, true)

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener {
            mCurAnimValue = it.animatedValue as Float

            invalidate()
        }

        animator.duration = 2000
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

        val stop: Float = mPathMeasure.length * mCurAnimValue

        mDstPath.reset()
        mPathMeasure.getSegment(0f, stop, mDstPath, true)
        canvas.drawPath(mDstPath, mPaint)

        /**
         * 箭头旋转实现方式1
         */
        /* mPathMeasure.getPosTan(stop, pos, tan)
         val degree = Math.atan2(tan[1].toDouble(), tan[0].toDouble()) * 180f / Math.PI
         mMatrix.reset()
         mMatrix.postRotate(degree.toFloat(), bitmap.width / 2f, bitmap.height / 2f)
         mMatrix.postTranslate(pos[0] - bitmap.width / 2, pos[1] - bitmap.height / 2)
 */

        mPathMeasure.getMatrix(stop, mMatrix, PathMeasure.POSITION_MATRIX_FLAG or PathMeasure.TANGENT_MATRIX_FLAG)
        mMatrix.preTranslate(-bitmap.width / 2f, -bitmap.height / 2f)
        canvas.drawBitmap(bitmap, mMatrix, mPaint)
    }


}