package com.oddinstitute.motions

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


/*
NOTES

Single Keyframes are not allowed
All Motions have keyframes between 0 to 100, so maximum number of keyframes are 100 (0 to 99)

 */





class MainActivity : AppCompatActivity()
{
    lateinit var redView: DrawView

    val duration: Float = 5.5f

    var currentFrame = 55


    var currentTime = 0f

    var trackLayoutWidth : Int = 0
    var trackLayoutHeight : Int = 0

    var playableLayoutWidth : Int = 0
    var playableOffsetFromLayout = 0

    var frameWidth: Int = 0

    var motionClips: ArrayList<Clip> = arrayListOf()



    fun measureFrameWidth ()
    {
        // you need to add one so that the last frame shows
        frameWidth = (playableLayoutWidth.toFloat() / (duration.toFrames() + 1 ).toFloat()).roundToInt()
    }


    fun placeTimeTick()
    {
        val ratio =
                (playableLayoutWidth.toFloat() / (duration.toFrames() + 1))  // Time.toFrames(shotTime)
                        .toFloat() // 2.45

        val place = currentFrame.toFloat() * ratio


        val myParams = RelativeLayout.LayoutParams(/*pixelsFromDp(3)*/ frameWidth,
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

    fun addMotionClips ()
    {
        motionClips.clear()

        for (data in redView.motionData)
        {
            data.clip = Clip()

            data.motion.calculateStartLength()

            data.clip = Clip(this,
                            duration.toFrames(),
                            playableOffsetFromLayout,
                             data,
                            trackLayoutHeight,
                            playableLayoutWidth,
                            trackLayoutWidth,
                            motionsTrackLayout)
        }
    }


    fun viewTreeListener () : ViewTreeObserver.OnGlobalLayoutListener
    {
        return object : OnGlobalLayoutListener
        {
            override fun onGlobalLayout()
            {
                motionsTrackLayout.viewTreeObserver
                        .removeOnGlobalLayoutListener(this)

                trackLayoutWidth =
                        motionsTrackLayout.width
                trackLayoutHeight =
                        motionsTrackLayout.height

                playableLayoutWidth =
                        playableTimeline.width

                // do these after finding the widths
                addMotionClips()
                measureFrameWidth()
                placeTimeTick ()

                // time is at current frame, so let's update
                playBack (redView)
            }
        }
    }

    fun playBackAll()
    {
        playBack(redView)
    }

    fun playBack(view: View)
    {
        timeTextView.text =
                "${currentFrame.toSeconds() /*Time.toSeconds(progress)*/}\n${currentFrame}"

        placeTimeTick()

        var txOfAllMotions = 0f
        var numberOfMotionsWithTx = 0
        var tyOfAllMotions = 0f
        var numberOfMotionsWithTy = 0

        for (data in redView.motionData)
        {
            // TX - because single keyframes are not allowed
            if (data.motion.translateX.playbackFrames.count() > 0)
            {
                numberOfMotionsWithTx += 1
                txOfAllMotions += data.motion.translateX.playbackFrames[currentFrame]
            }

            // TX - because single keyframes are not allowed
            if (data.motion.translateY.playbackFrames.count() > 0)
            {
                numberOfMotionsWithTy += 1
                tyOfAllMotions += data.motion.translateY.playbackFrames[currentFrame]
            }
        }

        if (numberOfMotionsWithTx != 0)
        {
            view.translationX = (txOfAllMotions / numberOfMotionsWithTx.toFloat())
        }

        if (numberOfMotionsWithTy != 0)
        {
            view.translationY = (tyOfAllMotions / numberOfMotionsWithTy.toFloat())
        }

    }


    private fun setupSeekbar ()
    {
        timeSeekbar.max = duration.toFrames()
        timeSeekbar.progress = currentFrame
    }


    fun seekBarListener () : SeekBar.OnSeekBarChangeListener
    {
//        timeTextView.text = "${ /*Time.toSeconds(timeSeekbar.progress)*/ timeSeekbar.progress.toSeconds()}\n${timeSeekbar.progress}"
        timeTextView.text = "${timeSeekbar.progress.toSeconds()}"

        return object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar,
                                           progress: Int,
                                           b: Boolean)
            {
                currentFrame = progress
                playBackAll()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
                // Do something
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentTime = currentFrame.toSeconds()
        setupSeekbar ()


        addTempView()


        timeSeekbar.setOnSeekBarChangeListener(seekBarListener())

        motionsTrackLayout.viewTreeObserver.addOnGlobalLayoutListener(viewTreeListener())


        setupPlayableTimeline ()
    }


    private fun setupPlayableTimeline ()
    {
        val playableParams = playableTimeline.layoutParams as RelativeLayout.LayoutParams

        playableParams.setMargins(playableMargins,
                                  16,
                                  playableMargins,
                                  16)
        playableTimeline.layoutParams = playableParams

        playableOffsetFromLayout = playableMargins
    }


    fun addTempView ()
    {
       redView =
                DrawView(this,
                         Color.RED)
        val myParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                     ViewGroup.LayoutParams.WRAP_CONTENT)

        myParams.width = pixelsFromDp(128)
        myParams.height = pixelsFromDp(128)

        myParams.leftMargin = pixelsFromDp(100)
        myParams.topMargin = pixelsFromDp(100)

        redView.layoutParams = myParams

        redView.motionData = AppData.makeDummyMotions(duration.toFrames()) // Time.toFrames(shotTime))
        boom.addView(redView)
    }
}