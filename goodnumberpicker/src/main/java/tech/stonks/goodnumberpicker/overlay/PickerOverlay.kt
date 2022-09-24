package tech.stonks.goodnumberpicker.overlay

import android.graphics.Canvas
import android.graphics.Rect
import tech.stonks.goodnumberpicker.GoodNumberPicker
import tech.stonks.goodnumberpicker.styles.GoodNumberPickerStyle

interface PickerOverlay {
    fun styleChanged(style: GoodNumberPickerStyle)
    fun draw(canvas: Canvas, centerItemRect: Rect)
}