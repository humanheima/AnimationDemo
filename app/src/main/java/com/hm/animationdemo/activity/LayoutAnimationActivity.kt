package com.hm.animationdemo.activity

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import kotlinx.android.synthetic.main.activity_layout_animation.*

class LayoutAnimationActivity : AppCompatActivity() {

    companion object {


        fun launch(context: Context) {
            val intent = Intent(context, LayoutAnimationActivity::class.java)
            context.startActivity(intent)
        }
    }


    private var leftPosition = 0

    private var rightPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_layout_animation)

        transition1()

        btnAdd.setOnClickListener {

            //addLeftView()
            addRightView()
        }

        btnRemove.setOnClickListener {
            //removeLeftView()
            removeRightView()
        }

    }

    private fun transition1() {
        val transition = LayoutTransition()

        val animIn = ObjectAnimator.ofFloat(null, "rotation", 0f, 1f)
        transition.setAnimator(LayoutTransition.APPEARING, animIn)

        val animOut = ObjectAnimator.ofFloat(null, "rotation", 0f, 90f, 0f)

        transition.setAnimator(LayoutTransition.DISAPPEARING, animOut)
        transition.setDuration(2000)

        llRightContainer.layoutTransition = transition
    }


    private fun addLeftView() {
        val button = Button(this)
        button.text = "button$leftPosition"
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        button.layoutParams = layoutParams
        llLeftContainer.addView(button, leftPosition++)
    }

    private fun removeLeftView() {
        if (leftPosition > 0) {
            llLeftContainer.removeViewAt(0)
            leftPosition--
        }
    }

    private fun addRightView() {
        val button = Button(this)
        button.text = "button$rightPosition"
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        button.layoutParams = layoutParams
        llRightContainer.addView(button, rightPosition++)
    }

    private fun removeRightView() {
        if (rightPosition > 0) {
            llRightContainer.removeViewAt(0)
            rightPosition--
        }
    }

}
