package com.hm.animationdemo.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.hm.animationdemo.R
import com.hm.animationdemo.evaluator.CircleViewEvaluator
import kotlinx.android.synthetic.main.activity_point_view.*

class PointViewActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val starter = Intent(context, PointViewActivity::class.java)
            context.startActivity(starter)
        }
    }


    private var curPoint: Point = Point()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_view)
        btnStart.setOnClickListener {
            //pointView.startAnimation()
            circleViewAnim()
        }

    }

    private fun circleViewAnim() {
        val animator = ValueAnimator.ofObject(CircleViewEvaluator(), Point(0, 0), Point(500, 500))
        animator.addUpdateListener {
            curPoint = it.animatedValue as Point
            ivCircle.layout(curPoint.x, curPoint.y, curPoint.x + ivCircle.width, curPoint.y + ivCircle.height)
        }
        animator.duration = 2000
        animator.start()
    }

}
