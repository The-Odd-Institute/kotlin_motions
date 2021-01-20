package com.oddinstitute.motions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView


class Clip(context: Context,
           var totalFrames: Int,
           var motion: Motion,
           var holderHeight: Int,
           var holderWidth: Int,
        /*var marginLeft: Int,*/
           var layout: RelativeLayout)
{
    private var xDelta = 0
    var previousX = 0

    var mainClip: TextView
    var leftHandle: View
    var rightHandle: View

    var marginLeft = 0


    var curWidth: Int = 0
    var previousWidth : Int = 0

    var widthAtTouchDown = 1
    var widthAtTouchUp = 1


    var widthRatio = 1.0f

    fun widthToLength(width: Float) : Int
    {
        // 500 of 1500
        // x of 137

        return ((totalFrames * width) / holderWidth.toFloat()).toInt()
    }

    fun lengthToWidth (length: Int) : Float
    {
        // 50 of 137
        // x of 1500

        return (length.toFloat() / totalFrames.toFloat()) * holderWidth
    }


    init
    {
        mainClip = TextView(context)
        mainClip.text = motion.name
        mainClip.gravity = Gravity.CENTER
        mainClip.setTextColor(Color.BLACK)
        mainClip.textSize = 14f


        motion.calculateStartEndLength()
        curWidth = lengthToWidth(motion.length).toInt()
//        curWidth = widthToLength(totalFrames, holderWidth).toInt()




        val myParams =
                RelativeLayout.LayoutParams(curWidth,
                                            ViewGroup.LayoutParams.MATCH_PARENT)

//        marginLeft = widthToLength(totalFrames, holderWidth).toInt()
//        marginLeft = lengthToWidth(motion.startFrame, holderWidth)
        marginLeft = lengthToWidth(motion.startFrame).toInt()

//        Log.d("MyTag", "total widht is: ${holderWidth}\nstartframe is: ${motion.startFrame}\nmargine is: ${marginLeft}\nlength is: ${motion.length}\nwidth is: $curWidth")


        myParams.setMargins(marginLeft,
                            0,
                            0,
                            0)
        mainClip.layoutParams = myParams
        mainClip.setBackgroundColor(motion.color)
        mainClip.setOnTouchListener(onTouchListener())
        layout.addView(mainClip)


        leftHandle = View(context)
        val leftHandleParams =
                RelativeLayout.LayoutParams(holderHeight / 2,
                                            holderHeight)
        leftHandleParams.marginStart = marginLeft
//        leftHandleParams.setMargins(marginLeft, 0, 0, 0)
        leftHandle.layoutParams = leftHandleParams
        leftHandle.setBackgroundColor(Color.argb(.3f,
                                                 .8f,
                                                 .8f,
                                                 0f))
        leftHandle.setOnTouchListener(onTouchListener())
        layout.addView(leftHandle)


        rightHandle = View(context)
        val rightHandleParams =
                RelativeLayout.LayoutParams(holderHeight / 2,
                                            holderHeight)
        rightHandleParams.marginStart = marginLeft + curWidth - holderHeight / 2
//        rightHandleParams.setMargins(marginLeft + width - height / 2 , 0, 0, marginLeft)
        rightHandle.layoutParams = rightHandleParams
        rightHandle.setBackgroundColor(Color.argb(.3f,
                                                  .8f,
                                                  .8f,
                                                  0f))
        rightHandle.setOnTouchListener(onTouchListener())
        layout.addView(rightHandle)


        previousWidth = curWidth
    }

    fun refreshClip()
    {


        val leftHandleParams: RelativeLayout.LayoutParams =
                leftHandle.layoutParams as RelativeLayout.LayoutParams
        leftHandleParams.leftMargin = marginLeft
        leftHandle.layoutParams = leftHandleParams


        val rightHandleParams: RelativeLayout.LayoutParams =
                rightHandle.layoutParams as RelativeLayout.LayoutParams
        rightHandleParams.marginStart = marginLeft + curWidth - holderHeight / 2
        rightHandle.layoutParams = rightHandleParams


        val mainClipParams: RelativeLayout.LayoutParams =
                mainClip.layoutParams as RelativeLayout.LayoutParams
        mainClipParams.width = curWidth
        mainClipParams.leftMargin = marginLeft
        mainClip.layoutParams = mainClipParams


        previousWidth = curWidth

    }

    fun updateMotion ()
    {
        widthRatio = widthAtTouchUp.toFloat() / widthAtTouchDown.toFloat()


        val newLength = widthToLength(curWidth.toFloat())
        val newStartFrame = widthToLength(marginLeft.toFloat())



        for (i in 0 until AppData.viewMotions.count())
        {
            var testMotion = AppData.viewMotions[i]

            if (testMotion.id == motion.id)
            {
                testMotion.motionResized(newStartFrame, newLength)
                AppData.makeFramesForMotion(testMotion, totalFrames)
                AppData.viewMotions[i] = testMotion
                break
            }

        }


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
                            val newWidth = curWidth - changeOfX
                            curWidth = newWidth
                        }
                        rightHandle ->
                        {
                            val newWidth = curWidth + changeOfX
                            curWidth = newWidth
                            marginLeft = movedViewLeftMargin + holderHeight / 2 - curWidth
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
                    updateMotion ()
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