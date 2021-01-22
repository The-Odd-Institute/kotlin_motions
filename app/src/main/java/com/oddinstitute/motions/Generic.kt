package com.oddinstitute.motions

import kotlin.math.roundToInt



var frameRate = 50


fun Float.toFrames(): Int
{
    return (this * frameRate).roundToInt()
}



fun Int.toSeconds (): Float
{

    return (((this.toFloat() / frameRate) * 10).roundToInt()).toFloat() / 10f

}