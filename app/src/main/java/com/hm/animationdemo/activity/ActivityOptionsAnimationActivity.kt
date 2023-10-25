package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import com.hm.animationdemo.chapter5.SVGActivity

/**
 * Created by p_dmweidu on 2023/6/29
 * Desc: 测试使用ActivityOptions实现Activity跳转动画
 */

class ActivityOptionsAnimationActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context, bundle: Bundle? = null) {
            val starter = Intent(context, ActivityOptionsAnimationActivity::class.java)
            context.startActivity(starter, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options_animation)
    }

    fun onClick(view: View){
        when(view.id){
            R.id.btnSvg -> {
                SVGActivity.launch(this)
            }

        }

    }

}