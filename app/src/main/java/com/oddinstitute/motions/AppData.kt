package com.oddinstitute.motions

import android.graphics.Color

class AppData
{
    companion object
    {
        // this is dummy data
        fun makeDummyMotions(length: Int) : ArrayList<MotionData>
        {
            var allMotionsData: ArrayList<MotionData> = arrayListOf()

            val keyframeTranslateX_1 =
                    Keyframe(0,
                             100f)
            val keyframeTranslateX_2 =
                    Keyframe(32,
                             180f)

            val keyframeTranslateX_3 =
                    Keyframe(275,
                             900f)

            val moveRightMotion = Motion()

            moveRightMotion.translateX.addKeyframe(keyframeTranslateX_1)
            moveRightMotion.translateX.addKeyframe(keyframeTranslateX_2)
            moveRightMotion.translateX.addKeyframe(keyframeTranslateX_3)

            moveRightMotion.name = "Move Right"
            moveRightMotion.color =
                    Color.argb(0.5f,
                               .8f,
                               .1f,
                               .1f)
            val moveRightMotionData = MotionData(java.util.UUID.randomUUID().toString(),
                                                 moveRightMotion)

            allMotionsData.add(moveRightMotionData)

            val keyframeTranslateY_1 =
                    Keyframe(32,
                             100f)
            val keyframeTranslateY_2 =
                    Keyframe(38,
                             300f)

            val keyframeTranslateY_3 =
                    Keyframe(49,
                             -400f)

            val keyframeTranslateY_4 =
                    Keyframe(77,
                             300f)


            val moveDownMotion = Motion()
            moveDownMotion.translateY.addKeyframe(keyframeTranslateY_1)
            moveDownMotion.translateY.addKeyframe(keyframeTranslateY_2)
            moveDownMotion.translateY.addKeyframe(keyframeTranslateY_3)
            moveDownMotion.translateY.addKeyframe(keyframeTranslateY_4)


            moveDownMotion.name = "Move Down"
            moveDownMotion.color =
                    Color.argb(0.5f,
                               .1f,
                               .1f,
                               .8f)
            val moveDownMotionData = MotionData(java.util.UUID.randomUUID().toString(), moveDownMotion)



            allMotionsData.add(moveDownMotionData)


            for (data in allMotionsData)
            {
                data.motion.makePlaybackFrames(length)
            }


            return allMotionsData
        }

    }
}