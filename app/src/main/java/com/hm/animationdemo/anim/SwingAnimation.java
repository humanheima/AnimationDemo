package com.hm.animationdemo.anim;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Crete by dumingwei on 2019/3/8
 * Desc: 钟摆动画
 * 参考链接：https://cloud.tencent.com/developer/article/1384616
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
        float leftPos = (float) (1.0 / 4.0);
        float rightPos = (float) (3.0 / 4.0);
        if (interpolatedTime <= leftPos) {
            //在1/4的时间要走完角度(mLeftDegrees - mMiddleDegrees)
            degrees = mMiddleDegrees + ((mLeftDegrees - mMiddleDegrees) * interpolatedTime * 4);
        } else if (interpolatedTime < rightPos) {
            //在1/2的时间要走完(mRightDegrees - mLeftDegrees)
            degrees = mLeftDegrees + ((mRightDegrees - mLeftDegrees) * (interpolatedTime - leftPos) * 2);
        } else {
            //在1/4的时间内要走完(mMiddleDegrees - mRightDegrees)
            degrees = mRightDegrees + ((mMiddleDegrees - mRightDegrees) * (interpolatedTime - rightPos) * 4);
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