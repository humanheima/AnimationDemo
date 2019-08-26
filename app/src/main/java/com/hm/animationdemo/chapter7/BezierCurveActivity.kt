package com.hm.animationdemo.chapter7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R

/**
 * Crete by dumingwei on 2019-08-25
 * Desc: 贝塞尔曲线
 *
 */
class BezierCurveActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BezierCurveActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bezier_curve)
    }
}
