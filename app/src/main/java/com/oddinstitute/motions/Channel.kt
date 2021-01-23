package com.oddinstitute.motions

class Channel(type: ChannelType)
{
    var channelOffset: Int = 0

    var actualKeyframes: ArrayList<Keyframe> = arrayListOf()
    var displayKeyframes: ArrayList<Keyframe> = arrayListOf()
    var playbackFrames: MutableList<Float> = mutableListOf()

    fun addKeyframe(keyframe: Keyframe)
    {
        if (actualKeyframes.count() == 0)
        {
            actualKeyframes.add(keyframe)
            return
        }
        else
        {
            for (i in 0 until actualKeyframes.count())
            {
                if (actualKeyframes[i].frame == keyframe.frame)
                {
                    // this exists
                    actualKeyframes[i] = keyframe
                    sortIt()
                    return
                }
            }

            actualKeyframes.add(keyframe)
            sortIt()
        }
    }

    fun sortIt()
    {
        val sorted = actualKeyframes.sortedWith(compareBy
                                                { it.frame })
        actualKeyframes.clear()
        for (keyframe in sorted)
        {
            actualKeyframes.add(keyframe)
        }
    }

    fun makeDisplayKeyframe (motionOffset: Int)
    {
        sortIt()

        displayKeyframes.clear()

        for (keyframe in actualKeyframes)
        {
            val newOffsetFrame = keyframe.frame + motionOffset + channelOffset
            val displayKeyframe = Keyframe(newOffsetFrame, keyframe.value)
            displayKeyframes.add(displayKeyframe)
        }
    }


    fun makePlaybackFrames(length: Int, motionOffset: Int)
    {
        makeDisplayKeyframe (motionOffset)
        playbackFrames.clear()

        if (this.displayKeyframes.count() < 2)
            return

        // fill the frames
        for (i in 0..length)
        {
            playbackFrames.add(0f)
        }


        val firstKeyFrame = this.displayKeyframes.first()
        val lastKeyframe = this.displayKeyframes.last()

        for (frameIndex in 0 until firstKeyFrame.frame)
        {
            playbackFrames[frameIndex] = firstKeyFrame.value
        }

        for (frameIndex in lastKeyframe.frame..length)
        {
            playbackFrames[frameIndex] = lastKeyframe.value
        }

        val lastIndex = this.displayKeyframes.count() - 1

        for (index in 0 until lastIndex)
        {
            val curKeyframe = this.displayKeyframes[index]
            val curValue = curKeyframe.value
            val curFrame = curKeyframe.frame

            val nextKeyframe = this.displayKeyframes[index + 1]
            val nextValue = nextKeyframe.value
            val nextFrame = nextKeyframe.frame

            val diffValue = nextValue - curValue
            val diffFrame = nextFrame - curFrame

            val increment = diffValue / diffFrame.toFloat()

            for (frameIndex in curFrame until nextFrame)
            {
                if (frameIndex in 0 until length)
                {
                    val newValue = curValue + increment * (frameIndex - curFrame)

                    playbackFrames[frameIndex] = newValue
                }
            }
        }
    }
}
