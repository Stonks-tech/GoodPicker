package tech.stonks.goodnumberpicker.example.overlay

import android.graphics.Canvas
import android.graphics.Rect

interface PickerOverlay {
    fun draw(canvas: Canvas, centerItemRect: Rect)
}