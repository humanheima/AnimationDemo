package com.hm.animationdemo.anim;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Crete by dumingwei on 2019/3/8
 * Desc: 钟摆动画
 * 参考链接：https://cloud.tencent.com/developer/article/1384616
 * <p>
 * leftDegree >0; rightDegree <0; 中间角度0度。 向左旋转角度是大于0的，向右旋转角度小于0；
 * 整个动画运动轨迹
 * 向左旋转30度 运动时间 1/8
 * 向右旋转60度 运动时间 1/4
 * 向左旋转60度 运动时间 1/4
 * 向右旋转60度 运动时间 1/4
 * 向左旋转30度回到起点 运动时间 1/8
 */
public class SwingAnimation extends Animation {

    private static final String TAG = "SwingAnimation";
    private float mMiddleDegrees;
    private float mLeftDegrees;
    private float mRightDegrees;
    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;
    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;
    private float mPivotX;
    private float mPivotY;

    public SwingAnimation(float middleDegrees, float leftDegrees, float rightDegrees) {
        mMiddleDegrees = middleDegrees;
        mLeftDegrees = leftDegrees;
        mRightDegrees = rightDegrees;
        mPivotX = 0.0f;
        mPivotY = 0.0f;
    }

    public SwingAnimation(float middleDegrees, float leftDegrees, float rightDegrees, float pivotX, float pivotY) {
        mMiddleDegrees = middleDegrees;
        mLeftDegrees = leftDegrees;
        mRightDegrees = rightDegrees;
        mPivotXType = ABSOLUTE;
        mPivotYType = ABSOLUTE;
        mPivotXValue = pivotX;
        mPivotYValue = pivotY;
        initializePivotPoint();
    }

    public SwingAnimation(float middleDegrees, float leftDegrees, float rightDegrees, int pivotXType, float pivotXValue,
                          int pivotYType, float pivotYValue) {
        mMiddleDegrees = middleDegrees;
        mLeftDegrees = leftDegrees;
        mRightDegrees = rightDegrees;
        mPivotXValue = pivotXValue;
        mPivotXType = pivotXType;
        mPivotYValue = pivotYValue;
        mPivotYType = pivotYType;
        initializePivotPoint();
    }

    private void initializePivotPoint() {
        if (mPivotXType == ABSOLUTE) {
            mPivotX = mPivotXValue;
        }
        if (mPivotYType == ABSOLUTE) {
            mPivotY = mPivotYValue;
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Log.d(TAG, "applyTransformation: " + interpolatedTime);
        float degrees;
        float leftPos = (float) (1.0 / 8.0);
        float rightPos = (float) (3.0 / 8.0);
        float leftLeftPos = (float) (5.0 / 8.0);
        float rightRightPos = (float) (7.0 / 8.0);
        if (interpolatedTime <= leftPos) {
            //在1/8的时间要走完角度(mLeftDegrees - mMiddleDegrees)
            degrees = mMiddleDegrees + ((mLeftDegrees - mMiddleDegrees) * interpolatedTime * 8);
        } else if (interpolatedTime < rightPos) {
            //在1/4的时间要走完(mRightDegrees - mLeftDegrees)
            degrees = mLeftDegrees + ((mRightDegrees - mLeftDegrees) * (interpolatedTime - leftPos) * 4);
        } else if (interpolatedTime < leftLeftPos) {
            //在1/4的时间要走完(mLeftDegrees-mRightDegrees)
            degrees = mRightDegrees + ((mLeftDegrees - mRightDegrees) * (interpolatedTime - rightPos) * 4);
        } else if (interpolatedTime < rightRightPos) {
            //在1/4的时间要走完(mRightDegrees - mLeftDegrees)
            degrees = mLeftDegrees + ((mRightDegrees - mLeftDegrees) * (interpolatedTime - leftLeftPos) * 4);
        } else {
            //在1/8的时间内要走完(mRightDegrees - mMiddleDegrees)
            degrees = mRightDegrees + ((mMiddleDegrees - mRightDegrees) * (interpolatedTime - rightRightPos) * 8);
        }
        Log.d(TAG, "applyTransformation:" + "degrees=" + degrees);

        float scale = getScaleFactor();
        if (mPivotX == 0.0f && mPivotY == 0.0f) {
            t.getMatrix().setRotate(degrees);
        } else {
            t.getMatrix().setRotate(degrees, mPivotX * scale, mPivotY * scale);
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }
}