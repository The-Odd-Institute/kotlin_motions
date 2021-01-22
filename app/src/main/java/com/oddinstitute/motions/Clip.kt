package com.oddinstitute.motions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

class Clip
{
    private var xDelta = 0
    var previousX = 0

    lateinit var mainClip: TextView
    lateinit var leftHandle: View
    lateinit var rightHandle: View

    var scaleMotion = false
    var offsetMotion = false

    var marginLeft = 0


    var width: Int = 0
    var previousWidth: Int = 0

    var widthAtTouchDown = 1
    var widthAtTouchUp = 1


    var xAtTouchDown = 0
    var xAtTouchUp = 0


    var widthRatio = 1.0f

    lateinit var context: Context
    var playableFramesCount: Int = 0
    var playableMarginToContainer: Int = 0
    lateinit var motionData: MotionData
    var containerHeight: Int = 0
    var playableLayoutWidth: Int = 0
    var containerWidth: Int = 0
    lateinit var containerLayout: RelativeLayout



    constructor ()
    {
        // tis is NEEDED
    }


    constructor (context: Context,
                 playableFramesCount: Int,
                 playableMarginToContainer: Int,
                 motionData: MotionData,
                 containerHeight: Int,
                 playableLayoutWidth: Int,
                 containerWidth: Int,
                 containerLayout: RelativeLayout)
    {
        this.context = context
        this.playableFramesCount = playableFramesCount
        this.playableMarginToContainer = playableMarginToContainer
        this.motionData = motionData
        this.containerHeight = containerHeight
        this.playableLayoutWidth = playableLayoutWidth
        this.containerWidth = containerWidth
        this.containerLayout = containerLayout


        mainClip = TextView(context)
        mainClip.text = motionData.motion.name
        mainClip.gravity = Gravity.CENTER
        mainClip.setTextColor(Color.BLACK)
        mainClip.textSize = 14f


        motionData.motion.calculateStartLength()
//        width = lengthToWidth(motion.length).toInt()
        width = lengthToWidth(motionData.motion.clipLength)


        val myParams =
                RelativeLayout.LayoutParams(width,
                                            ViewGroup.LayoutParams.MATCH_PARENT)

//        marginLeft = widthToLength(totalFrames, holderWidth).toInt()
//        marginLeft = lengthToWidth(motion.startFrame, holderWidth)
        marginLeft = lengthToWidth(motionData.motion.clipStart) + playableMarginToContainer

//        Log.d("MyTag", "total widht is: ${holderWidth}\nstartframe is: ${motion.startFrame}\nmargine is: ${marginLeft}\nlength is: ${motion.length}\nwidth is: $curWidth")


        myParams.setMargins(marginLeft,
                            0,
                            0,
                            0)
        mainClip.layoutParams = myParams
        mainClip.setBackgroundColor(motionData.motion.color)
        mainClip.setOnTouchListener(onTouchListener())
        containerLayout.addView(mainClip)


        leftHandle = View(context)
        val leftHandleParams =
                RelativeLayout.LayoutParams(containerHeight / 2,
                                            containerHeight)
        leftHandleParams.marginStart = marginLeft
//        leftHandleParams.setMargins(marginLeft, 0, 0, 0)
        leftHandle.layoutParams = leftHandleParams
        leftHandle.setBackgroundColor(Color.argb(.3f,
                                                 .8f,
                                                 .8f,
                                                 0f))
        leftHandle.setOnTouchListener(onTouchListener())
        containerLayout.addView(leftHandle)


        rightHandle = View(context)
        val rightHandleParams =
                RelativeLayout.LayoutParams(containerHeight / 2,
                                            containerHeight)
        rightHandleParams.marginStart = marginLeft + width - containerHeight / 2
//        rightHandleParams.setMargins(marginLeft + width - height / 2 , 0, 0, marginLeft)
        rightHandle.layoutParams = rightHandleParams
        rightHandle.setBackgroundColor(Color.argb(.3f,
                                                  .8f,
                                                  .8f,
                                                  0f))
        rightHandle.setOnTouchListener(onTouchListener())
        containerLayout.addView(rightHandle)


//        val keyFrames: ArrayList<Keyframe> = arrayListOf()
//
//        for (key in motion.tx)
//        {
//            keyFrames.add(key)
//        }
//        for (key in motion.ty)
//        {
//
//        }


        bringToFont()


        previousWidth = width
    }



    fun widthToLength(width: Int): Int
    {
        // 500 of 1500
        // x of 137

        return ((playableFramesCount * width).toFloat() / playableLayoutWidth.toFloat()).toInt()
    }

    fun lengthToWidth(length: Int): Int
    {
        // 50 of 137
        // x of 1500

        return ((length.toFloat() / playableFramesCount.toFloat()) * playableLayoutWidth).toInt()
    }




    fun refreshClip()
    {
        val leftHandleParams: RelativeLayout.LayoutParams =
                leftHandle.layoutParams as RelativeLayout.LayoutParams
        leftHandleParams.leftMargin = marginLeft
        leftHandle.layoutParams = leftHandleParams


        val rightHandleParams: RelativeLayout.LayoutParams =
                rightHandle.layoutParams as RelativeLayout.LayoutParams
        rightHandleParams.marginStart = marginLeft + width - containerHeight / 2
        rightHandle.layoutParams = rightHandleParams


        val mainClipParams: RelativeLayout.LayoutParams =
                mainClip.layoutParams as RelativeLayout.LayoutParams
        mainClipParams.width = width
        mainClipParams.leftMargin = marginLeft
        mainClip.layoutParams = mainClipParams

        previousWidth = width
    }

//    fun updateMotion() // : Motion
//    {
//        widthRatio = widthAtTouchUp.toFloat() / widthAtTouchDown.toFloat()
//
//        val newLength = widthToLength(width)
////        val newStartFrame = widthToLength(marginLeft)
//
//        val newOffset = widthToLength(marginLeft)
//
//        motionData.motion.motionResized(newStartFrame, newLength)
//
//
//
//
//        motionData.motion.makePlaybackFrames(playableFramesCount)
//
//
//        Log.d("Tag", "After update, we have: ${motionData.motion.translateX.playbackFrames.count()} X frames")
//
//
//
////        for (i in 0 until AppData.viewMotions.count())
////        {
////            var motion = AppData.viewMotions[i]
////
////            if (motion.id == this.motionData.id)
////            {
////                motion.motionResized(newStartFrame,
////                                     newLength)
////                motion.makePlaybackFrames(playableFramesCount)
////                AppData.viewMotions[i] = motion
////                break
////            }
////
////        }
//    }

    fun newUpdateMotion() // : Motion
    {
        if (widthAtTouchDown == 0)
        {
            return
        }
        // scale changed

            // this is the scale of the clip
            motionData.motion.scale = widthAtTouchUp.toFloat() / widthAtTouchDown.toFloat()

            val offset = xAtTouchUp - xAtTouchDown
            // this is the new start for the entirety f the clip
            // not what gets drawn
            motionData.motion.motionOffset += widthToLength(offset)


            motionData.motion.resizeMotionDisplay()



        motionData.motion.makePlaybackFrames(playableFramesCount)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchListener(): View.OnTouchListener?
    {
        return View.OnTouchListener { view, event ->
            val x = event.rawX.toInt()

            when (event.action and MotionEvent.ACTION_MASK)
            {
                MotionEvent.ACTION_DOWN ->
                {
                    val lParams = view.layoutParams as RelativeLayout.LayoutParams
                    xDelta = x - lParams.leftMargin

                    previousX = event.rawX.toInt()

                    bringToFont()

                    widthAtTouchDown = lParams.width
                    xAtTouchDown = lParams.leftMargin
                }

                MotionEvent.ACTION_MOVE ->
                {
                    val movedViewLeftMargin = x - xDelta
                    val changeOfX = event.rawX.toInt() - previousX
                    previousX = event.rawX.toInt()

                    when (view)
                    {
                        leftHandle ->
                        {
                            marginLeft = movedViewLeftMargin
                            val newWidth = width - changeOfX
                            width = newWidth
                        }
                        rightHandle ->
                        {
                            val newWidth = width + changeOfX
                            width = newWidth
                            marginLeft = movedViewLeftMargin + containerHeight / 2 - width
                        }
                        else ->
                        {
                            marginLeft = movedViewLeftMargin
                        }
                    }

                    refreshClip()
                    bringToFont()
                }

                MotionEvent.ACTION_UP ->
                {
                    val lParams = view.layoutParams as RelativeLayout.LayoutParams
                    widthAtTouchUp = lParams.width

                    xAtTouchUp = lParams.leftMargin


                    newUpdateMotion()
                }
            }
            true
        }
    }


    fun bringToFont()
    {
        mainClip.bringToFront()
        leftHandle.bringToFront()
        rightHandle.bringToFront()
    }
}