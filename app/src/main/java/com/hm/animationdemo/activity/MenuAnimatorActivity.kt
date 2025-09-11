package com.hm.animationdemo.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import kotlinx.android.synthetic.main.activity_menu_animator.item1
import kotlinx.android.synthetic.main.activity_menu_animator.item2
import kotlinx.android.synthetic.main.activity_menu_animator.item3
import kotlinx.android.synthetic.main.activity_menu_animator.item4
import kotlinx.android.synthetic.main.activity_menu_animator.item5
import kotlin.math.cos
import kotlin.math.sin

/**
 * Crete by dumingwei on 2019-08-22
 * Desc: 《Android自定义控件开发入门与实战》第三章示例 路径动画
 *
 */
class MenuAnimatorActivity : AppCompatActivity() {


    private var mIsMenuOpen = false

    companion object {

        private const val TAG = "MenuAnimatorActivity"

        fun launch(context: Context) {
            val intent = Intent(context, MenuAnimatorActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_animator)
    }

    fun onClick(view: View) {
        if (!mIsMenuOpen) {
            mIsMenuOpen = true
            openMenu()
        } else {
            Toast.makeText(this, "你点击了$view", Toast.LENGTH_SHORT).show()
            mIsMenuOpen = false
            closeMenu()
        }
    }


    private fun openMenu() {
        doAnimateOpen(item1, 0, 5, 300)
        doAnimateOpen(item2, 1, 5, 300)
        doAnimateOpen(item3, 2, 5, 300)
        doAnimateOpen(item4, 3, 5, 300)
        doAnimateOpen(item5, 4, 5, 300)

    }

    /**
     * @param radius，圆形菜单的半径，最多能移动多远
     */
    private fun doAnimateOpen(view: View, index: Int, total: Int, radius: Int) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }
        //转化为 弧度
        val toRadians = Math.toRadians(90.0)
        //角度最大为90度[0,90]
        /**
         * sin(0) = 0
         * cos(0) = 1
         *
         * sin(90) = 1.0
         * cos(90) = 0
         *
         */
        val degree = toRadians / (total - 1) * index
        val translationX = -(radius * sin(degree)).toFloat()
        val translationY = -(radius * cos(degree)).toFloat()
        Log.d(
            TAG,
            "doAnimateOpen: toRadians = $toRadians, degree = $degree, translationX = $translationX, translationY = $translationY"
        )
        val set = AnimatorSet()
        set.playTogether(
            ObjectAnimator.ofFloat(view, "translationX", 0f, translationX),
            ObjectAnimator.ofFloat(view, "translationY", 0f, translationY),
            ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
            ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
            ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        )
        set.duration = 500
        set.start()
    }


    private fun closeMenu() {
        doAnimateClose(item1, 0, 5, 300)
        doAnimateClose(item2, 1, 5, 300)
        doAnimateClose(item3, 2, 5, 300)
        doAnimateClose(item4, 3, 5, 300)
        doAnimateClose(item5, 4, 5, 300)
    }

    private fun doAnimateClose(view: View, index: Int, total: Int, radius: Int) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }
        val degree = Math.PI * index / ((total - 1) * 2)

        val translationX = -(radius * sin(degree)).toFloat()
        val translationY = -(radius * cos(degree)).toFloat()
        val set = AnimatorSet()
        set.playTogether(
            ObjectAnimator.ofFloat(view, "translationX", translationX, 0f),
            ObjectAnimator.ofFloat(view, "translationY", translationY, 0f),
            ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
            ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
            ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        )
        set.duration = 500
        set.start()
    }


}
