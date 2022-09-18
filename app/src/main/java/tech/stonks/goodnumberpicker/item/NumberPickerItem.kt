package tech.stonks.goodnumberpicker.item

import android.graphics.Canvas

interface NumberPickerItem {
    fun draw(canvas: Canvas, y: Float, width: Int, height: Int)
}