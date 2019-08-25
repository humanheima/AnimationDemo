package com.hm.animationdemo.chapter5

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.hm.animationdemo.R
import kotlinx.android.synthetic.main.activity_svg.*

/**
 * Crete by dumingwei on 2019-08-25
 * Desc:
 *
 */
class SVGActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, SVGActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_svg)

        val animatedVectorDrawable = AnimatedVectorDrawableCompat.create(this,
                R.drawable.line_animated_vector)
        ivDynamic.setImageDrawable(animatedVectorDrawable)
        (ivDynamic.drawable as Animatable).start()

        ivAnimSearch.isFocusable = true

        ivAnimSearch.isFocusableInTouchMode = true
        ivAnimSearch.requestFocus()
        edit.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                val animatedVectorDrawable = AnimatedVectorDrawableCompat.create(this,
                        R.drawable.search_animated_vector)
                ivAnimSearch.setImageDrawable(animatedVectorDrawable)
                (ivAnimSearch.drawable as Animatable).start()
            }
        }

    }
}
