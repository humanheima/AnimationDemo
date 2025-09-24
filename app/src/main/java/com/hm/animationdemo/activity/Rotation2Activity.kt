package com.hm.animationdemo.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R

/**
 * Created by p_dmweidu on 2025/9/24
 * Desc: 实现 Android 中两个 View（A 和 B）同时绕 Y 轴旋转。A旋转过后展示B
 */
class Rotation2Activity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, Rotation2Activity::class.java)
            context.startActivity(starter)
        }
    }

    private val viewA by lazy { findViewById<View>(R.id.viewA) }
    private val viewB by lazy { findViewById<View>(R.id.viewB) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rotate_animation2)


        val density = resources.displayMetrics.density
        val cameraDistance = density * 10000

        // 设置透视效果
        viewA.cameraDistance = cameraDistance
        viewB.cameraDistance = cameraDistance

        // 点击触发翻转动画
        viewA.setOnClickListener {
            //startFlipAnimation()
            startFlipAnimation2()
        }
        viewB.setOnClickListener {
            //startFlipAnimation()
            startFlipAnimation2()
        }
    }

    private fun startFlipAnimation() {
        // 当前正面是 viewA 还是 viewB
        val isViewAFront = viewA.visibility == View.VISIBLE

        // 动画目标 View 和角度
        val viewToAnimate = if (isViewAFront) viewA else viewB
        val startAngle = if (isViewAFront) 0f else 180f
        val endAngle = if (isViewAFront) 180f else 360f

        // 创建 Y 轴旋转动画
        val animator =
            ObjectAnimator.ofFloat(viewToAnimate, "rotationY", startAngle, endAngle).apply {
                duration = 3000 // 动画时长
            }

        // 在旋转到 90 度时切换 View
        animator.addUpdateListener { animation ->
            val rotation = animation.animatedValue as Float
            if (isViewAFront) {
                if (rotation >= 90f && viewA.visibility == View.VISIBLE) {
                    viewA.visibility = View.GONE
                    viewB.visibility = View.VISIBLE
                }
            } else {
                if (rotation <= 270f && viewB.visibility == View.VISIBLE) {
                    viewB.visibility = View.GONE
                    viewA.visibility = View.VISIBLE
                }
            }
        }

        // 动画结束时确保状态正确
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                if (isViewAFront) {
                    viewA.rotationY = 0f
                    viewB.rotationY = 180f
                } else {
                    viewA.rotationY = 0f
                    viewB.rotationY = 180f
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animator.start()
    }

    private fun startFlipAnimation2() {
        // 当前正面是 viewA 还是 viewB
        val isViewAFront = viewA.visibility == View.VISIBLE

        // 创建 A 和 B 的旋转动画
        val animatorA = ObjectAnimator.ofFloat(
            viewA,
            "rotationY",
            if (isViewAFront) 0f else 180f,
            if (isViewAFront) 180f else 360f
        ).apply {
            duration = 1000
        }

        val animatorB = ObjectAnimator.ofFloat(
            viewB,
            "rotationY",
            if (isViewAFront) 180f else 360f,
            if (isViewAFront) 360f else 540f
        ).apply {
            duration = 1000
        }

        // 组合动画
        val animatorSet = AnimatorSet().apply {
            playTogether(animatorA, animatorB)
        }

        // 在旋转到 90 度（或 270 度）时切换可见性
        animatorA.addUpdateListener { animation ->
            val rotation = animation.animatedValue as Float
            if (isViewAFront) {
                if (rotation >= 90f && viewA.visibility == View.VISIBLE) {
                    viewA.visibility = View.GONE
                    viewB.visibility = View.VISIBLE
                }
            } else {
                if (rotation >= 270f && viewB.visibility == View.VISIBLE) {
                    viewB.visibility = View.GONE
                    viewA.visibility = View.VISIBLE
                }
            }
        }

        // 动画结束时重置角度
        animatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                //viewA.rotationY = if (isViewAFront) 0f else 180f
                //viewB.rotationY = if (isViewAFront) 180f else 0f
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        animatorSet.start()
    }
}