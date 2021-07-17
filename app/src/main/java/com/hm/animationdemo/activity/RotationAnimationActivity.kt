package com.hm.animationdemo.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import com.hm.animationdemo.anim.Rotate3dAnimation

/**
 * Created by dumingwei on 2021/7/14
 *
 * Desc: 测试翻转动画
 */
class RotationAnimationActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private var animatorSet: AnimatorSet? = null

    private lateinit var ivRotationY: ImageView
    private lateinit var ivRotationY2: ImageView
    private lateinit var ivRotationY3: ImageView

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, RotationAnimationActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rotate_animation)

        ivRotationY = findViewById(R.id.ivRotationY)
        ivRotationY2 = findViewById(R.id.ivRotationY2)
        ivRotationY3 = findViewById(R.id.ivRotationY3)

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnStartRotationAnimation1 -> {
                rotateMethod1()
                //rotateMethod2()
            }
            R.id.btnStartRotationAnimation2 -> {
                val rotationXAnimator = ObjectAnimator.ofFloat(ivRotationY2, "rotationX", 0f, 360f)
                val density = resources.displayMetrics.density
                val cameraDistance = density * 10000
                ivRotationY2.cameraDistance = cameraDistance

                rotationXAnimator.duration = 3000

                rotationXAnimator.start()
            }
            R.id.btnStartRotationAnimation3 -> {
                val rotationXAnimator = ObjectAnimator.ofFloat(ivRotationY3, "rotation", 0f, 360f)
                val density = resources.displayMetrics.density
                val cameraDistance = density * 10000
                ivRotationY2.cameraDistance = cameraDistance

                rotationXAnimator.duration = 3000

                rotationXAnimator.start()
            }

        }
    }

    private fun rotateMethod1() {
        Log.i(TAG, "onClick: ivRotationY.cameraDistance = ${ivRotationY.cameraDistance}")

        val density = resources.displayMetrics.density
        val cameraDistance = density * 10000

        Log.i(TAG, "onClick: densityDpi = $density  cameraDistance = $cameraDistance")

        ivRotationY.cameraDistance = cameraDistance

        val rotationYAnimator = ObjectAnimator.ofFloat(ivRotationY, "rotationY", 0f, 360f)
        rotationYAnimator.duration = 3000
        rotationYAnimator.start()
    }

    /**
     * 感觉这种方式并不好使。
     */
    private fun rotateMethod2() {
        //val depth = 400f
        //val depth = 0f
        val depth = ivRotationY.measuredWidth / 2f
        val rotate3dAnimation = Rotate3dAnimation(0f, 90f, ivRotationY.measuredWidth / 2f, ivRotationY.measuredHeight / 2f, depth, false)
        rotate3dAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                val rotateAnimation = Rotate3dAnimation(270f, 360f, ivRotationY.measuredWidth / 2f, ivRotationY.measuredHeight / 2f, depth, true)
                rotateAnimation.duration = 1000
                rotateAnimation.fillAfter = true
                ivRotationY.startAnimation(rotateAnimation);
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        rotate3dAnimation.duration = 1000
        rotate3dAnimation.fillAfter = true
        ivRotationY.startAnimation(rotate3dAnimation)
    }

    override fun onStop() {
        super.onStop()
        animatorSet?.cancel()
    }

}
