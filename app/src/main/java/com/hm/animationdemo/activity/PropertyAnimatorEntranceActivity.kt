package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import com.hm.animationdemo.chapter5.PathAnimatorActivity

/**
 * Crete by dumingwei on 2019-08-22
 * Desc: 属性动画入口
 *
 */
class PropertyAnimatorEntranceActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, PropertyAnimatorEntranceActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animator_entrance)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnTestRotateAnimation -> {
                RotationAnimationActivity.launch(this)
            }
            R.id.btnBase -> {
                PropertyAnimationActivity.launch(this)
            }
            R.id.btnMenu -> {
                MenuAnimatorActivity.launch(this)
            }
            R.id.btnPointView -> {
                PointViewActivity.launch(this)
            }
            R.id.btnViewPropertyAnimator -> {
                ViewPropertyAnimatorActivity.launch(this)
            }

            R.id.btnPath -> {
                PathAnimatorActivity.launch(this)
            }
        }

    }
}
