package tech.stonks.goodnumberpicker.item

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class TextNumberPickerItem(private val _text: String) : NumberPickerItem {
    companion object {
        private val defaultPaint: Paint = Paint().apply {
            color = Color.BLACK
            textSize = 50f
        }
    }

    private var _paint: Paint = defaultPaint
    private var _textBounds: Rect = calculateTextBounds()


    /**
     * param y is the y coordinate of the top of the item
     */
    override fun draw(canvas: Canvas, y: Float, width: Int, height: Int) {
        canvas.drawText(
            _text,
            (width - _textBounds.width()) / 2f,
            y + ((height + _textBounds.height()) /2f),
            _paint
        )
    }

    private fun calculateTextBounds(): Rect{
        val bounds = Rect()
        _paint.getTextBounds(_text, 0, _text.length, bounds)
        return bounds
    }

    override fun toString(): String {
        return _text
    }
}