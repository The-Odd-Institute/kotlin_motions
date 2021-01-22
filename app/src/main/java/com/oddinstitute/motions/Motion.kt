package com.oddinstitute.motions

import android.graphics.Color
import android.util.Log
import kotlin.math.roundToInt


class MotionData (val id: String, var motion: Motion)
{
    var clip: Clip? = null
}


class Motion
{
    var motionOffset: Int = 0

    var clipStart = 100_000
    var scale: Float = 1.0f
    var clipLength: Int = 0
    var name: String = "Motion"
    var color: Int = Color.TRANSPARENT



    var translateX: Channel = Channel(ChannelType.TranslateX)
    var translateY: Channel = Channel(ChannelType.TranslateY)


    var channels: Array<Channel> = arrayOf(translateX, translateY)


    fun makePlaybackFrames (length: Int)
    {
        translateX.makePlaybackFrames(length, motionOffset)
        translateY.makePlaybackFrames(length, motionOffset)

//        for (channel in channels)
//        {
//            channel.makePlaybackFrames(length)
//        }
    }

    fun calculateStartLength()
    {
        var end: Int = 0
        for (channel in channels)
        {
            for (any in channel.displayKeyframes)
            {
                if (any.frame < clipStart)
                    clipStart = any.frame

                if (any.frame > end)
                    end = any.frame
            }
        }
        clipLength = end - clipStart
    }


    fun resizeMotionDisplay ()
    {
        translateX.displayKeyframes.clear()

        // because actual keyframes are sorted, these are sorted
        for (each in translateX.actualKeyframes)
        {
            val newFrame = (each.frame  * scale).roundToInt()
            val newKeyframe = Keyframe(newFrame, each.value)

            translateX.displayKeyframes.add(newKeyframe)
        }


        translateY.displayKeyframes.clear()

        // because actual keyframes are sorted, these are sorted
        for (each in translateY.actualKeyframes)
        {
            val newFrame = (each.frame  * scale).roundToInt()
            val newKeyframe = Keyframe(newFrame, each.value)

            translateY.displayKeyframes.add(newKeyframe)
        }
    }

    /*
    fun scaleMotion ()
    {

        var newChannelTranslateX: Channel = Channel(ChannelType.TranslateX)

        for (any in translateX.displayKeyframes)
        {
            val newFrame = ((any.frame + startOffset) * scale).roundToInt()
            val newKeyframe = Keyframe(newFrame, any.value)

            newChannelTranslateX.addKeyframe(newKeyframe)
        }
        newChannelTranslateX.sortIt()
        this.translateX = newChannelTranslateX

        var newChannelTranslateY: Channel = Channel(ChannelType.TranslateY)

        for (any in translateY.keyframes)
        {
            val newFrame = ((any.frame + startOffset) * scale).roundToInt()
            val newKeyframe = Keyframe(newFrame, any.value)

            newChannelTranslateY.addKeyframe(newKeyframe)
        }
        newChannelTranslateY.sortIt()
        this.translateY = newChannelTranslateY

        clipLength = newLength
        curStart = newStartFrame
    }

     */


//    fun motionResized(newStartFrame: Int, newLength: Int)
//    {
//        if (newStartFrame == curStart && newLength == clipLength)
//            return
//
//        scale = newLength.toFloat() / clipLength.toFloat()
//
//        val startOffset = newStartFrame - curStart
//
//        var newChannelTranslateX: Channel = Channel(ChannelType.TranslateX)
//
//        for (any in translateX.keyframes)
//        {
//            val newFrame = ((any.frame + startOffset) * scale).roundToInt()
//            val newKeyframe = Keyframe(newFrame, any.value)
//
//            newChannelTranslateX.addKeyframe(newKeyframe)
//        }
//        newChannelTranslateX.sortIt()
//        this.translateX = newChannelTranslateX
//
//        var newChannelTranslateY: Channel = Channel(ChannelType.TranslateY)
//
//        for (any in translateY.keyframes)
//        {
//            val newFrame = ((any.frame + startOffset) * scale).roundToInt()
//            val newKeyframe = Keyframe(newFrame, any.value)
//
//            newChannelTranslateY.addKeyframe(newKeyframe)
//        }
//        newChannelTranslateY.sortIt()
//        this.translateY = newChannelTranslateY
//
//        clipLength = newLength
//        curStart = newStartFrame
//    }
}
