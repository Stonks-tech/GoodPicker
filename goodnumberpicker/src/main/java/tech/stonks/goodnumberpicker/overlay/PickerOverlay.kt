package tech.stonks.goodnumberpicker.overlay

import android.graphics.Canvas
import android.graphics.Rect
import tech.stonks.goodnumberpicker.GoodNumberPicker

interface PickerOverlay {
    fun styleChanged(style: GoodNumberPicker.Style)
    fun draw(canvas: Canvas, centerItemRect: Rect)
}