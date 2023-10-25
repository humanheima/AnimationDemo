package com.hm.animationdemo.widget;

import android.graphics.drawable.AnimationDrawable;

/**
 * Created by p_dmweidu on 2023/10/25
 * Desc: 监听帧动画的结束
 */
public class AnimationDrawable2 extends AnimationDrawable {

    /**
     * 动画结束的次数，因为帧动画可能会播放一次，或者一直播放
     */
    private int finishCount = 0;


    private IAnimationFinishListener animationFinishListener;

    public IAnimationFinishListener getAnimationFinishListener() {
        return animationFinishListener;
    }

    public void setAnimationFinishListener(IAnimationFinishListener animationFinishListener) {
        this.animationFinishListener = animationFinishListener;
    }

    @Override
    public void start() {
        super.start();
        if (animationFinishListener != null) {
            animationFinishListener.onAnimationStart();
        }
    }

    @Override
    public boolean selectDrawable(int index) {
        boolean ret = super.selectDrawable(index);
        if ((index != 0) && (index == getNumberOfFrames() - 1)) {
            if (finishCount >= 10) {
                setOneShot(true);
            }
            if (animationFinishListener != null) {
                animationFinishListener.onAnimationFinished(++finishCount);
            }
        }
        return ret;
    }

    public interface IAnimationFinishListener {

        void onAnimationStart();

        /**
         * 动画结束
         *
         * @param endCount 第一次结束
         */
        void onAnimationFinished(int endCount);
    }

}