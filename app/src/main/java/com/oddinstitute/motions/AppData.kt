package com.oddinstitute.motions

import android.graphics.Color

class AppData
{
    companion object
    {

        var viewMotions: ArrayList<Motion> = arrayListOf()


        fun makeDummyMotions(length: Int)
        {
            val keyframeTranslateX_1 =
                    Keyframe(0,
                             100f)
            val keyframeTranslateX_2 =
                    Keyframe(32,
                             180f)

            val keyframeTranslateX_3 =
                    Keyframe(45,
                             900f)

            val moveRightMotion = Motion(java.util.UUID.randomUUID().toString())
            moveRightMotion.name = "Move Right"
            moveRightMotion.color =
                    Color.argb(0.5f,
                               .8f,
                               .1f,
                               .1f)
            moveRightMotion.tx.add(keyframeTranslateX_1)
            moveRightMotion.tx.add(keyframeTranslateX_2)
            moveRightMotion.tx.add(keyframeTranslateX_3)

            viewMotions.add(moveRightMotion)


            val keyframeTranslateY_1 =
                    Keyframe(45,
                             100f)
            val keyframeTranslateY_2 =
                    Keyframe(87,
                             300f)

            val keyframeTranslateY_3 =
                    Keyframe(107,
                             -400f)

            val keyframeTranslateY_4 =
                    Keyframe(137,
                             300f)

            val moveDownMotion = Motion(java.util.UUID.randomUUID().toString())
            moveDownMotion.color =
                    Color.argb(0.5f,
                               .1f,
                               .1f,
                               .8f)

            moveDownMotion.ty.add(keyframeTranslateY_1)
            moveDownMotion.ty.add(keyframeTranslateY_2)
            moveDownMotion.ty.add(keyframeTranslateY_3)
            moveDownMotion.ty.add(keyframeTranslateY_4)
            moveDownMotion.name = "Move Down"

            viewMotions.add(moveDownMotion)


            for (i in 0 until viewMotions.count())
            {
                viewMotions[i] = makeFramesForMotion(viewMotions[i], length)
            }

        }



        fun makeFramesForChannel (keyframes: ArrayList<Keyframe>, length: Int) : ArrayList <SimpleFrame>
        {

            var frames = ArrayList<SimpleFrame>()

            if (keyframes.count() == 0 )
            {
//            Log.d(TAG, "No Keyframes")
            }
            else if (keyframes.count() == 1)
            {
//            Log.d(TAG, "One Keyframe")

                val keyframe = keyframes[0]
                for (i in 0..length)
                {
                    val frame = SimpleFrame(keyframe.value)
                    frames.add(frame)
                }
            }
            else // 2 or more keyframes
            {
//            Log.d(TAG, "Many Keyframes")

                // fill the frames
                for (i in 0..length)
                {
                    frames.add(SimpleFrame(0f))
                }

                val firstKeyFrame = keyframes.first()
                val lastKeyframe = keyframes.last()

                for (frameIndex in 0 until firstKeyFrame.frame)
                {
                    frames[frameIndex] = SimpleFrame(firstKeyFrame.value)
                }

                for (frameIndex in lastKeyframe.frame..length)
                {
                    frames[frameIndex] = SimpleFrame(lastKeyframe.value)
                }

                val lastIndex = keyframes.count() - 1

                for (index in 0 until lastIndex)
                {
                    val curKeyframe = keyframes[index]
                    val curValue = curKeyframe.value
                    val curFrame = curKeyframe.frame

                    val nextKeyframe = keyframes[index + 1]
                    val nextValue = nextKeyframe.value
                    val nextFrame = nextKeyframe.frame

                    val diffValue = nextValue - curValue
                    val diffFrame = nextFrame - curFrame

                    val increment = diffValue / diffFrame.toFloat()

                    for (frameIndex in curFrame until nextFrame)
                    {
                        val newValue = curValue + increment * (frameIndex - curFrame)
                        val newFrame = SimpleFrame(newValue)

                        frames[frameIndex] = newFrame

//                    Log.d(TAG, "Val is: ${newValue}")
                    }
                }
            }
            return frames
        }



        fun makeFramesForMotion (motion : Motion, length: Int) : Motion
        {
            val newMotion = motion

            newMotion.txF = makeFramesForChannel(motion.tx, length)
            newMotion.tyF = makeFramesForChannel(motion.ty, length)

            return newMotion
        }


    }
}