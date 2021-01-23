package com.oddinstitute.motions

import kotlin.math.roundToInt

var frameRate = 50
var playableMargins = 80

fun Float.toFrames(): Int
{
    return (this * frameRate).roundToInt()
}

fun Int.toSeconds (): Float
{
    return (((this.toFloat() / frameRate) * 10).roundToInt()).toFloat() / 10f
}