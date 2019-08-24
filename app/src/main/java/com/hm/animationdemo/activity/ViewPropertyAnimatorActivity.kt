package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import kotlinx.android.synthetic.main.activity_view_property_animator.*

/**
 * Crete by dumingwei on 2019-08-24
 * Desc: ViewPropertyAnimator 可以使View能够更方便使用属性动画
 * 使用ViewPropertyAnimator最大的优势在于它提供的简明易读的代码书写方式
 *
 */
class ViewPropertyAnimatorActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ViewPropertyAnimatorActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_property_animator)
    }

    fun onClick(view: View) {
        when (view.id) {

            R.id.tvTestScaleYScaleYBy -> {
                tvScaleY.animate().scaleY(2f)
                tvScaleYBy.animate().scaleYBy(2f)
            }
        }

    }
}
