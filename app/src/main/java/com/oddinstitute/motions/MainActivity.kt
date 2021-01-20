package com.oddinstitute.motions

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


//enum class GroupChannel
//{
//    TranslateX,
//    TranslateY,
//
//    Rotate,
//
//    ScaleX,
//    ScaleY,
//
//    Alpha,
//}
//
//enum class PolyChannel
//{
//    Data,
//    Alpha,
//    FillColor,
//    StrokeColor
//}


//class CovarianceSample<T>


class MainActivity : AppCompatActivity()
{
    val TAG = "MyTag"
    val lengthInSeconds = 5.5f
    var currentTime = 1.1f
    var currentFrame = 0


    var motionsTrackLayoutWidth = 0
    var motionsTrackLayoutHeight = 0


    fun addMotionToTrack(motion: Motion)
    {
        motion.calculateStartEndLength()


        // we now have the start and end frames


//        val width =
//                (motion.length.toFloat() / Time.secondsToFrame(lengthInSeconds)
//                        .toFloat()) * motionsTrackLayoutWidth
//        val marginLeft =
//                (motion.startFrame.toFloat() / Time.secondsToFrame(lengthInSeconds)
//                        .toFloat()) * motionsTrackLayoutWidth


        val motionClip = Clip(this,
                              Time.secondsToFrame(lengthInSeconds),
                              motion,
                              motionsTrackLayoutHeight,
                              motionsTrackLayoutWidth,
                              motionsTrackLayout)

//        val motionClip = Clip(width.toInt(),
//                              motionsTrackLayoutHeight,
//                              marginLeft.toInt(),
//                              this,
//                              motion,
//                              motionsTrackLayout)


    }


//    fun makeAllFramesForMotion()
//    {
//        for (i in 0 until viewMotions.count())
//        {
//            val motion = viewMotions[i]
//            motion.txF.clear()
//            motion.tyF.clear()
//
//            if (motion.tx.count() > 0)
//            {
//                // make txF
//
//                var tempValue = 1f
//                for (i in 0..137)
//                {
//                    tempValue += 2.75f
//                    val frame = Frame(tempValue)
//                    motion.txF.add(frame)
//                }
//            }
//            if (motion.ty.count() > 0)
//            {
//                // make tyF
//
//                var tempValue1 = 1f
//                for (i in 0..137)
//                {
//                    tempValue1 += 4.75f
//                    val frame = Frame(tempValue1)
//                    motion.tyF.add(frame)
//                }
//            }
//
//            viewMotions[i] = motion
//        }
//    }

//    var viewMotions: ArrayList<Motion> = arrayListOf()


    fun placeTimeTick()
    {
        val ratio =
                motionsTrackLayoutWidth / Time.secondsToFrame(lengthInSeconds)
                        .toFloat() // 2.45

        val place = currentFrame.toFloat() * ratio


        val myParams = RelativeLayout.LayoutParams(pixelsFromDp(3),
                                                   ViewGroup.LayoutParams.MATCH_PARENT)


        myParams.setMargins(place.toInt(),
                            0,
                            0,
                            0)

        timeTick.bringToFront()
        timeTick.layoutParams = myParams

    }

    fun pixelsFromDp(dps: Int): Int
    {
        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                               dps.toFloat(),
                                               resources.displayMetrics)
        return pixels.toInt()
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppData.makeDummyMotions(Time.secondsToFrame(lengthInSeconds))


        motionsTrackLayout.viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener
                                           {
                                               override fun onGlobalLayout()
                                               {
                                                   motionsTrackLayout.viewTreeObserver
                                                           .removeOnGlobalLayoutListener(this)
                                                   motionsTrackLayoutWidth =
                                                           motionsTrackLayout.width
                                                   motionsTrackLayoutHeight =
                                                           motionsTrackLayout.height

                                                   for (i in 0 until AppData.viewMotions.count())
                                                   {
//                                                       if (i == 1 )
                                                       addMotionToTrack(AppData.viewMotions[i])
                                                   }
                                               }
                                           })


//        makeDummyMotions()


//        makeAllFramesForMotion()

//        moveVerticallyButton.setOnClickListener {
//
//            val ty1 = ObjectAnimator.ofFloat(redView,
//                                             View.TRANSLATION_X,
//                                             redView.translationX,
//                                             redView.translationX + 200f) // this way, we add
//            ty1.duration = 500
//            ty1.interpolator = LinearInterpolator() // BounceInterpolator()
//            ty1.start()
//
//        }


        timeSeekbar.max = Time.secondsToFrame(lengthInSeconds)
        timeSeekbar.progress = Time.secondsToFrame(currentTime)
        timeTextView.text = "${Time.framesToSeconds(timeSeekbar.progress)}\n${timeSeekbar.progress}"


        timeSeekbar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener
                {
                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int,
                                                   b: Boolean)
                    {
                        timeTextView.text = "${Time.framesToSeconds(progress)}\n${progress}"

                        currentFrame = progress
                        placeTimeTick()

                        var tx = 0f
                        var ty = 0f

                        var txCounter = 0
                        var tyCounter = 0

                        for (motion in AppData.viewMotions)
                        {
                            if (motion.txF.count() > 0)
                            {
                                txCounter += 1
                                tx = motion.txF[progress].value
                            }
                            if (motion.tyF.count() > 0)
                            {
                                tyCounter += 1
                                ty = motion.tyF[progress].value
                            }
                        }

                        if (txCounter != 0)
                        {
                            redView.translationX = (tx / txCounter.toFloat())
                        }

                        if (tyCounter != 0)
                        {
                            redView.translationY = (ty / tyCounter.toFloat())
                        }

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar)
                    {
                        // Do something

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar)
                    {
                        // Do something

                    }
                })


    }


}
