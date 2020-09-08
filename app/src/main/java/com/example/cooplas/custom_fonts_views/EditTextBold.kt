package com.example.cooplas.custom_fonts_views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText

class EditTextBold(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Lato-Bold.ttf")
    }
}