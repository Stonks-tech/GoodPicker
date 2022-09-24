package tech.stonks.goodpicker.overlay

import android.graphics.Canvas
import android.graphics.Rect
import tech.stonks.goodpicker.styles.GoodPickerStyle

interface PickerOverlay {
    fun styleChanged(style: GoodPickerStyle)
    fun draw(canvas: Canvas, centerItemRect: Rect)
}