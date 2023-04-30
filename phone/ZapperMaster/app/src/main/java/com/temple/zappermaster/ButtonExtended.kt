package com.temple.zappermaster

import android.content.Context
import androidx.appcompat.widget.AppCompatButton


class ButtonExtended(context: Context) : AppCompatButton(context) {
    var topPositionPercent: Double = 0.0
    var leftPositionPercent: Double = 0.0
    var code: String = ""
    var displayName: String = ""
}