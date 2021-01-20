package com.oddinstitute.motions

import android.graphics.Color
import android.util.Log


class Motion (val id: String)
{
    var startFrame: Int = 10_000
    var endFrame: Int = 0
    var length: Int = 0


    var name: String = "Motion"
    var color: Int = Color.TRANSPARENT


    var tx: ArrayList<Keyframe> = arrayListOf()
    var txF: ArrayList<Frame> = arrayListOf()

    var ty: ArrayList<Keyframe> = arrayListOf()
    var tyF: ArrayList<Frame> = arrayListOf()



    fun calculateStartEndLength ()
    {

        for (keyframe in this.tx)
        {
            if (keyframe.frame < startFrame)
                startFrame = keyframe.frame

            if (keyframe.frame > endFrame)
                endFrame = keyframe.frame
        }

        for (keyframe in this.ty)
        {
            if (keyframe.frame < startFrame)
                startFrame = keyframe.frame

            if (keyframe.frame > endFrame)
                endFrame = keyframe.frame
        }

        length = endFrame - startFrame
    }


//    var all


    // we need a system to make sure no two keyframes get added at the same frame
    // we need a way to add multiple keyframes

//    fun <T> addKeyframe (keyframe: Keyframe<T>)
//    {
//        when (keyframe.channel)
//        {
//            Channel.TranslateX -> translateX_Keys.add(keyframe as Keyframe<Float>)
//            Channel.TranslateY -> translateY_Keys.add(keyframe as Keyframe<Float>)
//            Channel.Rotate -> rotate_Keys.add(keyframe as Keyframe<Float>)
//            Channel.ScaleX -> scaleX_Keys.add(keyframe as Keyframe<Float>)
//            Channel.ScaleY -> scaleY_Keys.add(keyframe as Keyframe<Float>)
//            Channel.Alpha -> alpha_Keys.add(keyframe as Keyframe<Float>)
//            Channel.FillColor -> fillColor_Keys.add(keyframe as Keyframe<Int>)
//            Channel.StrokeColor -> strokeColor_Keys.add(keyframe as Keyframe<Int>)
//            else -> morph_Keys.add(keyframe as Keyframe<Any>)
//        }
//    }

    fun motionResized (newStartFrame: Int, newLength: Int)
    {
        if (newStartFrame == startFrame && newLength == length)
            return


        something ain't workig here'

        val lengthRatio = newLength.toFloat() / length.toFloat()
        // 1.2

        val startOffset = newStartFrame - startFrame


        Log.d("MyTag", "Start was: $startFrame\nNew Start is: $newStartFrame\nlength was: $length\nnewlength is: $newLength\noffset is: $startOffset\n ratio is: $lengthRatio")



        var newTx = arrayListOf<Keyframe>()
        for (keyframe in this.tx)
        {
            val newFrame = ((keyframe.frame + startOffset) * lengthRatio).toInt()
            val newKeyframe = Keyframe(newFrame, keyframe.value)
            newTx.add(newKeyframe)
        }
        this.tx = newTx


        var newTy = arrayListOf<Keyframe>()
        for (keyframe in this.ty)
        {
            val newFrame = ((keyframe.frame + startOffset) * lengthRatio).toInt()
            val newKeyframe = Keyframe(newFrame, keyframe.value)
            newTy.add(newKeyframe)
        }
        this.ty = newTy


        length = newLength
        startFrame = newStartFrame
    }

}
