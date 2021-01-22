package com.oddinstitute.motions

import android.content.Context
import android.graphics.*
import android.view.View





class DrawView : View
{

    var motionData : ArrayList<MotionData> = arrayListOf()


    constructor(context: Context?,
                bgColor: Int) : super(context)
    {
        this.setBackgroundColor(bgColor)

    }

    override fun onDraw(canvas: Canvas)
    {

    }
}