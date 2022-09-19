package tech.stonks.goodnumberpicker.item

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import tech.stonks.goodnumberpicker.GoodNumberPicker

class TextNumberPickerItem(
    private val _context: Context,
    private val _text: String,
    private var _style: GoodNumberPicker.Style
) : NumberPickerItem {

    private var _paint: Paint = Paint().apply {
        textAlign = Paint.Align.CENTER
    }
    private var _textBounds: Rect = calculateTextBounds()

    /**
     * param y is the y coordinate of the top of the item
     */
    override fun draw(canvas: Canvas, y: Float, width: Int, height: Int) {
        canvas.drawText(
            _text,
            (width - _textBounds.width()) / 2f,
            y + ((height + _textBounds.height()) / 2f),
            _paint
        )
    }

    override fun styleChanged(style: GoodNumberPicker.Style) {
        _style = style
        updatePaint()
    }

    private fun calculateTextBounds(): Rect {
        val bounds = Rect()
        _paint.getTextBounds(_text, 0, _text.length, bounds)
        return bounds
    }

    private fun updatePaint() {
        _paint.apply {
            val style = _style.textStyle
            color = style.textColor
            textSize = style.textSize
            typeface = if (style.font != null) {
                val font = _context.resources.getFont(style.font)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Typeface.create(
                        font,
                        _style.textStyle.fontWeight,
                        false
                    )
                } else {
                    font
                }
            } else {
                null
            }
        }
    }

    override fun toString(): String {
        return _text
    }

}