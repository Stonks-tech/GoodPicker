package tech.stonks.goodnumberpicker.item

import android.graphics.Canvas
import tech.stonks.goodnumberpicker.GoodNumberPicker
import tech.stonks.goodnumberpicker.styles.GoodNumberPickerStyle

interface NumberPickerItem {
    val style: GoodNumberPickerStyle
    fun draw(canvas: Canvas, y: Float, width: Int, height: Int)
    fun styleChanged(style: GoodNumberPickerStyle)
}