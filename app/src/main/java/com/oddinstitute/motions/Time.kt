package com.oddinstitute.motions


class Time
{
    companion object
    {
        fun secondsToFrame(timeInSeconds: Float): Int
        {
            return (timeInSeconds * 25).toInt()
        }

        fun framesToSeconds(frame: Int): Float
        {
            var timeInHalfSeconds = (frame.toFloat() / 25f)

            // 12 / 25 -> .48

            if (timeInHalfSeconds % .5 < .25)
                timeInHalfSeconds -= (timeInHalfSeconds % .5).toFloat()
            else
                timeInHalfSeconds += (.5 - timeInHalfSeconds % .5).toFloat()


            return timeInHalfSeconds
        }


    }
}
