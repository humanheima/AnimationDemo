package com.hm.animationdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.hm.animationdemo.R
import kotlinx.android.synthetic.main.activity_point_view.*

class PointViewActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val starter = Intent(context, PointViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_view)
        btnStart.setOnClickListener {
            pointView.startAnimation()
        }
    }

}
