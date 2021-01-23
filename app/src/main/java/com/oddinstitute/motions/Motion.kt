package com.oddinstitute.motions

import android.graphics.Color
import android.util.Log
import kotlin.math.roundToInt


// this class is a generic class that hold one to all sorts of channels
// that together build the motion of an object
/*
    Rotate ("Rotate"),
    ScaleX ("Resize Horizontally"),
    ScaleY ("Resize Vertically"),
    Data ("Shape"),
    FillColor ("Fill Color"),
    StrokeColor ("Border Color"),
    Alpha ("Visibility")
 */
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
    var rotate: Channel = Channel(ChannelType.Rotate)
    var scaleX: Channel = Channel(ChannelType.ScaleX)
    var scaleY: Channel = Channel(ChannelType.ScaleY)
    var data: Channel = Channel(ChannelType.Data)
    var fillColor: Channel = Channel(ChannelType.FillColor)
    var strokeColor: Channel = Channel(ChannelType.StrokeColor)
    var alpha: Channel = Channel(ChannelType.Alpha)


    private var channels: Array<Channel> = arrayOf()

    fun collectChannels()
    {
        channels = arrayOf(translateX,
                           translateY,
                           rotate,
                           scaleX,
                           scaleY,
                           data,
                           fillColor,
                           strokeColor,
                           alpha)
    }


    fun makePlaybackFrames(length: Int)
    {
//        translateX.makePlaybackFrames(length,
//                                      motionOffset)
//        translateY.makePlaybackFrames(length,
//                                      motionOffset)

        for (channel in channels)
        {
            channel.makePlaybackFrames(length,
                                       motionOffset)
        }
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


    fun resizeMotionDisplay()
    {
        for (channel in channels)
        {
            channel.displayKeyframes.clear()

            // because actual keyframes are sorted, these are sorted
            for (each in channel.actualKeyframes)
            {
                val newFrame = (each.frame * scale).roundToInt()
                val newKeyframe =
                        Keyframe(newFrame,
                                 each.value)

                channel.displayKeyframes.add(newKeyframe)
            }
        }


//        translateX.displayKeyframes.clear()
//
//        // because actual keyframes are sorted, these are sorted
//        for (each in translateX.actualKeyframes)
//        {
//            val newFrame = (each.frame * scale).roundToInt()
//            val newKeyframe =
//                    Keyframe(newFrame,
//                             each.value)
//
//            translateX.displayKeyframes.add(newKeyframe)
//        }
//
//
//        translateY.displayKeyframes.clear()
//
//        // because actual keyframes are sorted, these are sorted
//        for (each in translateY.actualKeyframes)
//        {
//            val newFrame = (each.frame * scale).roundToInt()
//            val newKeyframe =
//                    Keyframe(newFrame,
//                             each.value)
//
//            translateY.displayKeyframes.add(newKeyframe)
//        }
    }
}
