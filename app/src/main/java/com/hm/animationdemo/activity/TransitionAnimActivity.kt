package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import com.hm.animationdemo.R

/**
 * Created by p_dmweidu on 2023/7/2
 * Desc: 测试 Transition 动画
 */
class TransitionAnimActivity : AppCompatActivity() {

    private var ivScenery: ImageView? = null

    companion object {

        @JvmStatic
        fun launch(context: Context, bundle: Bundle) {
            val starter = Intent(context, TransitionAnimActivity::class.java)
            context.startActivity(starter, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.trans_slide)
        window.exitTransition = transition
        //Activity A 打开本Activity时，本Activity的入场动画
        window.enterTransition = transition
        //window.allowEnterTransitionOverlap = false
        //window.allowReturnTransitionOverlap = false
        //从本Activity 返回Activity A时，本Activity的出场动画
        //window.returnTransition = transition
        //从本Activity 返回Activity A时，Activity A的入场动画
        //window.reenterTransition = transition

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_anim)
        ivScenery = findViewById(R.id.iv_scenery)
        //ViewCompat.setTransitionName(ivScenery!!, "ivScenery")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }
}