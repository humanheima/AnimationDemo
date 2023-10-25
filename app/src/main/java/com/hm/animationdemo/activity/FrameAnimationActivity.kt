package com.hm.animationdemo.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.LevelListDrawable
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hm.animationdemo.R
import com.hm.animationdemo.widget.AnimationDrawable2
import com.hm.animationdemo.widget.LevelImageView


/**
 * Created by p_dmweidu on 2023/10/25
 * Desc: 测试帧动画
 */
class FrameAnimationActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "FrameAnimationActivity"

        fun launch(context: Context) {
            val starter = Intent(context, FrameAnimationActivity::class.java)
            context.startActivity(starter)
        }
    }

    private var ivFrameAnimation1: ImageView? = null
    private var ivFrameAnimation2: ImageView? = null
    private var tvFrameAnimation: TextView? = null


    private var resIdList: MutableList<Int> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_animation)
        ivFrameAnimation1 = findViewById(R.id.ivFrameAnimation1)
        ivFrameAnimation2 = findViewById(R.id.ivFrameAnimation2)

        tvFrameAnimation = findViewById(R.id.tvFrameAnimation)

        resIdList.add(R.drawable.bubble_frame1)
        resIdList.add(R.drawable.bubble_frame2)
        resIdList.add(R.drawable.bubble_frame3)
        resIdList.add(R.drawable.bubble_frame4)
        resIdList.add(R.drawable.bubble_frame5)
        resIdList.add(R.drawable.bubble_frame6)
        resIdList.add(R.drawable.bubble_frame7)
        resIdList.add(R.drawable.bubble_frame8)
        resIdList.add(R.drawable.bubble_frame9)
        resIdList.add(R.drawable.bubble_frame10)
        resIdList.add(R.drawable.bubble_frame11)
        resIdList.add(R.drawable.bubble_frame12)

        val animationDrawable = AnimationDrawable()
        val animationDrawable2 = AnimationDrawable2()
        resIdList.forEach {
            animationDrawable.addFrame(getDrawable(it), 100)
            animationDrawable2.addFrame(getDrawable(it), 100)
        }
        animationDrawable.isOneShot = false
        ivFrameAnimation1?.setImageDrawable(animationDrawable)
        animationDrawable.start()

        animationDrawable2.isOneShot = false
        ivFrameAnimation2?.setImageDrawable(animationDrawable2)
        Log.i(TAG, "onCreate: animationDrawable2 = $animationDrawable2")
        animationDrawable2.animationFinishListener =
            object : AnimationDrawable2.IAnimationFinishListener {

                override fun onAnimationStart() {
                    //Log.i(TAG, "onAnimationStart: ")
                }

                override fun onAnimationFinished(endCount: Int) {
                    //Log.i(TAG, "onAnimationFinished: endCount=$endCount")
                    if (endCount >= 10) {
                        ivFrameAnimation2?.drawable?.let {
                            if (it is AnimationDrawable) {
                                Log.i(TAG, "onAnimationFinished: $it")
                                it.getFrame(0)?.let { frame ->
                                    ivFrameAnimation2?.setImageDrawable(frame)
                                }
                            }
                        }
                    }
                }
            }

        animationDrawable2.start()

        testLayerDrawable()

    }

    private fun testLayerDrawable() {
        val imageView = LevelImageView(this)
        //设置level资源.
        val levelListDrawable = LevelListDrawable()
        levelListDrawable.addLevel(0, 0, ContextCompat.getDrawable(this, R.drawable.bubble_frame1))
        levelListDrawable.addLevel(1, 1, getDrawable(R.drawable.bubble_frame2))
        levelListDrawable.addLevel(2, 2, getDrawable(R.drawable.bubble_frame3))
        levelListDrawable.addLevel(3, 3, getDrawable(R.drawable.bubble_frame4))
        levelListDrawable.addLevel(4, 4, getDrawable(R.drawable.bubble_frame5))
        levelListDrawable.addLevel(5, 5, getDrawable(R.drawable.bubble_frame6))
        levelListDrawable.addLevel(6, 6, getDrawable(R.drawable.bubble_frame7))
        levelListDrawable.addLevel(7, 7, getDrawable(R.drawable.bubble_frame8))
        levelListDrawable.addLevel(8, 8, getDrawable(R.drawable.bubble_frame9))
        levelListDrawable.addLevel(9, 9, getDrawable(R.drawable.bubble_frame10))

        val headerAnimator = ObjectAnimator.ofInt(imageView, "imageLevel", 0, 9)
        //设置动画的播放数量为一直播放.
        headerAnimator.repeatCount = 3
        //设置一个速度加速器.让动画看起来可以更贴近现实效果.
        //设置一个速度加速器.让动画看起来可以更贴近现实效果.
        headerAnimator.interpolator = LinearInterpolator()
        headerAnimator.repeatMode = ObjectAnimator.RESTART
        headerAnimator.duration = 1000
        headerAnimator.start()

        headerAnimator.addUpdateListener {
            Log.i(TAG, "testLayerDrawable: it.animatedValue = ${it.animatedValue}")
            levelListDrawable.level = it.animatedValue as Int

        }
        headerAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                //动画结束后,将level设置为0,让动画可以继续播放.
                levelListDrawable.level = 0
                Log.i(TAG, "onAnimationEnd: ")
            }
        })

        tvFrameAnimation?.background = levelListDrawable

    }


}