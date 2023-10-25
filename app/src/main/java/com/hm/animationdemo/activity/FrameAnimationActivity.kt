package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import com.hm.animationdemo.widget.AnimationDrawable2

/**
 * Created by p_dmweidu on 2023/10/25
 * Desc: 测试帧动画
 */
class FrameAnimationActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "FrameAnimationActivity"

        fun launch(context: Context) {
            val starter = Intent(context, FrameAnimationActivity::class.java)
            context.startActivity(starter)
        }
    }

    private var ivFrameAnimation1: ImageView? = null
    private var ivFrameAnimation2: ImageView? = null


    private var resIdList: MutableList<Int> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_animation)
        ivFrameAnimation1 = findViewById(R.id.ivFrameAnimation1)
        ivFrameAnimation2 = findViewById(R.id.ivFrameAnimation2)

        resIdList.add(R.drawable.bubble_frame1)
        resIdList.add(R.drawable.bubble_frame2)
        resIdList.add(R.drawable.bubble_frame3)
        resIdList.add(R.drawable.bubble_frame4)
        resIdList.add(R.drawable.bubble_frame5)
        resIdList.add(R.drawable.bubble_frame6)
        resIdList.add(R.drawable.bubble_frame7)
        resIdList.add(R.drawable.bubble_frame8)
        resIdList.add(R.drawable.bubble_frame9)
        resIdList.add(R.drawable.bubble_frame10)
        resIdList.add(R.drawable.bubble_frame11)
        resIdList.add(R.drawable.bubble_frame12)

        val animationDrawable = AnimationDrawable()
        val animationDrawable2 = AnimationDrawable2()
        resIdList.forEach {
            animationDrawable.addFrame(getDrawable(it), 100)
            animationDrawable2.addFrame(getDrawable(it), 100)
        }
        animationDrawable.isOneShot = false
        ivFrameAnimation1?.setImageDrawable(animationDrawable)
        animationDrawable.start()

        animationDrawable2.isOneShot = false
        ivFrameAnimation2?.setImageDrawable(animationDrawable2)
        Log.i(TAG, "onCreate: animationDrawable2 = $animationDrawable2")
        animationDrawable2.animationFinishListener =
            object : AnimationDrawable2.IAnimationFinishListener {

                override fun onAnimationStart() {
                    Log.i(TAG, "onAnimationStart: ")
                }

                override fun onAnimationFinished(endCount: Int) {
                    Log.i(TAG, "onAnimationFinished: endCount=$endCount")
                    if (endCount >= 10) {
                        ivFrameAnimation2?.drawable?.let {
                            if (it is AnimationDrawable) {
                                Log.i(TAG, "onAnimationFinished: $it")
                                it.getFrame(0)?.let { frame ->
                                    ivFrameAnimation2?.setImageDrawable(frame)
                                }
                            }
                        }
                    }
                }
            }

        animationDrawable2.start()
    }


}