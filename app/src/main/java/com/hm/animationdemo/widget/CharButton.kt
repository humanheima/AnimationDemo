package com.hm.animationdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.Button

/**
 * Created by dumingwei on 2019-08-22.
 * Desc:
 */
class CharButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {


    var charText: Char? = null
        set(value) {
            field = value
            text = value.toString()
        }

}

