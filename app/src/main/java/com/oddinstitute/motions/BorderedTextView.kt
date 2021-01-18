package com.oddinstitute.motions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class BorderedTextView : View
{
    private val paint: Paint = Paint()
    private var borders: Array<Border>? = null

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
    {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    {
        init()
    }

    constructor(context: Context?) : super(context)
    {
        init()
    }

    private fun init()
    {
        paint.setStyle(Paint.Style.STROKE)
        paint.setColor(Color.BLACK)
        paint.setStrokeWidth(4f)
    }

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)
        if (borders == null) return
        for (border in borders!!)
        {
            paint.setColor(border.color)
            paint.setStrokeWidth(border.width.toFloat())
            if (border.style === BORDER_TOP)
            {
                canvas.drawLine(0f, 0f, width.toFloat(), 0f, paint)
            } else if (border.style === BORDER_RIGHT)
            {
                canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), paint)
            } else if (border.style === BORDER_BOTTOM)
            {
                canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), paint)
            } else if (border.style === BORDER_LEFT)
            {
                canvas.drawLine(0f, 0f, 0f, height.toFloat(), paint)
            }
        }
    }

    fun getBorders(): Array<Border>?
    {
        return borders
    }

    fun setBorders(borders: Array<Border>?)
    {
        this.borders = borders
    }

    companion object
    {
        const val BORDER_TOP = 0x00000001
        const val BORDER_RIGHT = 0x00000002
        const val BORDER_BOTTOM = 0x00000004
        const val BORDER_LEFT = 0x00000008
    }
}


class Border(var style: Int)
{
    var orientation = 0
    var width = 0
    var color = Color.BLACK

}