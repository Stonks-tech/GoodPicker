package tech.stonks.goodnumberpicker.item

import android.graphics.Canvas
import tech.stonks.goodnumberpicker.GoodNumberPicker

interface NumberPickerItem {
    fun draw(canvas: Canvas, y: Float, width: Int, height: Int)
    fun styleChanged(style: GoodNumberPicker.Style)
}