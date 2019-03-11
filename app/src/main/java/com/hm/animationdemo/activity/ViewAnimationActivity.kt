package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.RotateAnimation

import com.hm.animationdemo.R
import kotlinx.android.synthetic.main.activity_view_animation.*

/**
 * Crete by dumingwei on 2019/3/11
 * Desc: 视图动画 也叫补间动画
 *
 */
class ViewAnimationActivity : AppCompatActivity() {

    private var rotateAnimation: RotateAnimation? = null

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ViewAnimationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation)
        btnRotate.setOnClickListener {
            testRotateAnimation()
        }
    }

    /**
     * 角度大于0，顺时针方向旋转
     * 角度小于0，逆时针方向旋转
     */
    private fun testRotateAnimation() {
        if (rotateAnimation == null) {
            //相对于view自身做动画
            /*rotateAnimation = RotateAnimation(0F, -90F,
                    Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F)
            rotateAnimation?.duration = 1000L
            rotateAnimation?.fillAfter = true*/

            //相对于parent做动画
            rotateAnimation = RotateAnimation(0F, -90F,
                    Animation.RELATIVE_TO_PARENT, 0.5F, Animation.RELATIVE_TO_PARENT, 0.0F)
            rotateAnimation?.duration = 1000L
            rotateAnimation?.fillAfter = false

            //相对于绝对坐标做动画
            rotateAnimation = RotateAnimation(0F, -90F,
                    Animation.ABSOLUTE, btnRotate.width / 2.0F, Animation.ABSOLUTE, btnRotate.height / 2.0F)
            rotateAnimation?.duration = 1000L
            rotateAnimation?.fillAfter = false
        }
        btnRotate.startAnimation(rotateAnimation)
    }
}
