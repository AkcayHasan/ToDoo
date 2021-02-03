package com.hasanakcay.todoo.util

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.hasanakcay.todoo.R

class SlideAnim {
    fun slideUp(view: View, context: Context){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
    }
}