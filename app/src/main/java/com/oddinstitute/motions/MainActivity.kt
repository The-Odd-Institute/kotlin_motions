package com.oddinstitute.motions

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*


enum class Channel
{
    TranslateX,
    TranslateY,
    Rotate,
    ScaleX,
    ScaleY,
    Alpha,
    FillColor,
    StrokeColor,
    Morph,
}


class Keyframe(var frame: Int,
               var value: Float)

class Frame(var value: Float)

class Motion
{
    var tx: ArrayList<Keyframe> = arrayListOf()
    var txF: ArrayList<Frame> = arrayListOf()

var ty: ArrayList<Keyframe> = arrayListOf()
    var tyF: ArrayList<Frame> = arrayListOf()




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

}


class Time
{
    companion object
    {
        fun frame(timeInSeconds: Float): Int
        {
            return (timeInSeconds * 25).toInt()
        }

        fun time(frame: Int): Float
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


//class CovarianceSample<T>


class MainActivity : AppCompatActivity()
{
    val lengthInSeconds = 5.5f
    var currentTime = 1.1f



    var txLayoutWidth = 0


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        txLayout.viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener
                                           {
                                               override fun onGlobalLayout()
                                               {
                                                   txLayout.getViewTreeObserver()
                                                           .removeOnGlobalLayoutListener(this)
                                                   txLayoutWidth = txLayout.width

                                                   Log.d("MyTag", "width is: ${txLayoutWidth}")



                                                   var tempFrameGap = 35
                                                   var tempWidth = (tempFrameGap.toFloat() / Time.frame(lengthInSeconds).toFloat()) * txLayoutWidth


                                                   var tempStartFrame = 20
                                                   var tempMarginLeft = (tempStartFrame / Time.frame(lengthInSeconds).toFloat())   * txLayoutWidth


                                                   val clip = View(this@MainActivity)
                                                   val myParams = RelativeLayout.LayoutParams(tempWidth.toInt(),
                                                                                              ViewGroup.LayoutParams.MATCH_PARENT)


                                                   myParams.setMargins(tempMarginLeft.toInt(), 0, 0, 0)

                                                   clip.layoutParams = myParams


                                                   clip.setBackgroundColor(Color.GREEN)
                                                   txLayout.addView(clip)


                                               }
                                           })





        val keyframeTranslateX_1 =
                Keyframe(0,
                         100f)
        val keyframeTranslateX_2 =
                Keyframe(50,
                         200f)

        val moveRightMotion = Motion()
        moveRightMotion.tx.add(keyframeTranslateX_1)
        moveRightMotion.tx.add(keyframeTranslateX_2)



        val keyframeTranslateY_1 =
                Keyframe(0,
                         100f)
        val keyframeTranslateY_2 =
                Keyframe(50,
                         200f)

        val moveDownMotion = Motion()
        moveDownMotion.tx.add(keyframeTranslateY_1)
        moveDownMotion.tx.add(keyframeTranslateY_2)




        moveVerticallyButton.setOnClickListener {

            val ty1 = ObjectAnimator.ofFloat(redView,
                                             View.TRANSLATION_X,
                                             redView.translationX,
                                             redView.translationX + 200f) // this way, we add
            ty1.duration = 500
            ty1.interpolator = LinearInterpolator() // BounceInterpolator()
            ty1.start()

        }





        timeSeekbar.max = Time.frame(lengthInSeconds)
        timeSeekbar.progress = Time.frame(currentTime)
        timeTextView.text = "${Time.time(timeSeekbar.progress)}\n${timeSeekbar.progress}"


        timeSeekbar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener
                {
                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int,
                                                   b: Boolean)
                    {
                        timeTextView.text = "${Time.time(progress)}\n${progress}"
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
