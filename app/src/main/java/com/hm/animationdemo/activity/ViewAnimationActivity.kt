package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import kotlinx.android.synthetic.main.activity_view_animation.*
import kotlinx.android.synthetic.main.layout_round_rect_scanner_anim.*
import kotlinx.android.synthetic.main.layout_scanner_anim.*

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

    private lateinit var animation1: Animation
    private lateinit var animation2: Animation
    private lateinit var animation3: Animation
    private lateinit var animation4: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation)
        btnRotate.setOnClickListener {
            //loadFromXml(it, R.anim.rotateanim)
            rotate(it)
        }

        btnTranslate.setOnClickListener {
            //loadFromXml(it, R.anim.translate_anim)
            translate(it)
        }

        btnScale.setOnClickListener {
            //loadFromXml(it, R.anim.scaleanim)
            scale(it)
        }

        btnAlpha.setOnClickListener {
            //loadFromXml(it, R.anim.alphaanim)
            alpha(it)
        }

        btnAnimSet.setOnClickListener {
            //loadFromXml(it, R.anim.anim_set)
            animSet(it)
        }

        btnScaleImg.setOnClickListener {
            scaleImg()
        }

        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)
        animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)
        animation3 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)
        animation4 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)

        ivStartScan.setOnClickListener {

            circle1.startAnimation(animation1)

            animation2.startOffset = 600
            circle2.startAnimation(animation2)

            animation3.startOffset = 1200
            circle3.startAnimation(animation3)

            animation4.startOffset = 1800

            circle4.startAnimation(animation4)

        }

        ivRoundRectStartScan.setOnClickListener {
            round_rect_1.startAnimation(animation1)

            animation2.startOffset = 600
            round_rect_2.startAnimation(animation2)

            animation3.startOffset = 1200
            round_rect_3.startAnimation(animation3)

            animation4.startOffset = 1800

            round_rect_4.startAnimation(animation4)

        }

        ivframeAnim.setOnClickListener {
            val anim = ivframeAnim.background as AnimationDrawable
            anim.start()
        }

        ivframeAnimAnother.setOnClickListener {

            val anim = AnimationDrawable()
            for (i in 1..14) {
                val id = resources.getIdentifier("list_icon_gif_playing$i", "drawable", packageName)
                val drawable = resources.getDrawable(id)
                anim.addFrame(drawable, 60)
            }
            anim.isOneShot = false
            ivframeAnimAnother.setBackgroundDrawable(anim)
            anim.start()

        }
    }

    private fun loadFromXml(view: View, animRes: Int) {
        val animation = AnimationUtils.loadAnimation(this, animRes)
        view.startAnimation(animation)
    }

    private fun scale(view: View) {
        val animation = ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 3000
        animation.fillAfter = true
        view.startAnimation(animation)

    }

    private fun alpha(view: View) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 2000
        animation.fillAfter = true
        view.startAnimation(animation)
    }

    private fun rotate(view: View) {
        val animation = RotateAnimation(0f, 650f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 3000
        //animation.fillAfter = true
        view.startAnimation(animation)
    }

    private fun translate(view: View) {
        val animation = TranslateAnimation(Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, -80f, Animation.ABSOLUTE, 0f
                , Animation.ABSOLUTE, -80f)
        animation.duration = 1000
        animation.fillAfter = true
        view.startAnimation(animation)
    }

    private fun animSet(view: View) {
        val alphaAnim = AlphaAnimation(1.0f, 0.1f)
        alphaAnim.duration = 2000

//        val scaleAnim = ScaleAnimation(0f, 1.4f, 0f, 1.4f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

        val rotateAnim = RotateAnimation(0f, 720f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

        rotateAnim.startOffset = 2000
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(alphaAnim)
        //animationSet.addAnimation(scaleAnim)
        animationSet.addAnimation(rotateAnim)

        view.startAnimation(animationSet)
    }

    private fun scaleImg() {
        val animation = ScaleAnimation(0.0f, 1.2f, 0.0f, 1.2f
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 3000
        animation.fillAfter = true
        animation.interpolator = BounceInterpolator()
        ivScene.startAnimation(animation)
    }

    override fun onStop() {
        super.onStop()
        stopAnim(animation1)
        stopAnim(animation2)
        stopAnim(animation3)
        stopAnim(animation4)
    }

    private fun stopAnim(animation: Animation) {
        animation.cancel()
    }

}
