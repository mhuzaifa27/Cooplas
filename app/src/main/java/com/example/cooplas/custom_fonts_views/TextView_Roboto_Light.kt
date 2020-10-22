package com.example.cooplas.custom_fonts_views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class TextView_Roboto_Light(context: Context, attrs: AttributeSet) : TextView(context, attrs) {

    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Roboto-Light.ttf")
    }
}