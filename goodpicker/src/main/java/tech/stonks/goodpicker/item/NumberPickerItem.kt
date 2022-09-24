package tech.stonks.goodpicker.item

import android.graphics.Canvas
import tech.stonks.goodpicker.styles.GoodPickerStyle

interface PickerItem {
    val style: GoodPickerStyle
    fun draw(canvas: Canvas, y: Float, width: Int, height: Int)
    fun styleChanged(style: GoodPickerStyle)
}