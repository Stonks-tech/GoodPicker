package tech.stonks.goodnumberpicker.example.item

import android.graphics.Canvas
import tech.stonks.goodnumberpicker.example.GoodNumberPicker

interface NumberPickerItem {
    fun draw(canvas: Canvas, y: Float, width: Int, height: Int)
    fun styleChanged(style: GoodNumberPicker.Style)
}