package com.oddinstitute.motions

class Keyframe (var frame: Int, var value: Float)
{
    override fun toString(): String
    {
        return "frame: $frame -> $value"
    }
}