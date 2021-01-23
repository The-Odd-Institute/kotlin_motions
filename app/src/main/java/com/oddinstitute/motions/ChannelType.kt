package com.oddinstitute.motions


enum class ChannelType (val type: String)
{
    // Group Level Only
    TranslateX ("Move Horizontally"),
    TranslateY ("Move Vertically"),

    Rotate ("Rotate"),

    ScaleX ("Resize Horizontally"),
    ScaleY ("Resize Vertically"),



    // Poly Level Only
    Data ("Shape"),
    FillColor ("Fill Color"),
    StrokeColor ("Border Color"),


    // any Level
    Alpha ("Visibility")
}