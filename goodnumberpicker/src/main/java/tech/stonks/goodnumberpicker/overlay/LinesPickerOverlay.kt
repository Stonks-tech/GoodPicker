package tech.stonks.goodnumberpicker.overlay

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import tech.stonks.goodnumberpicker.GoodNumberPicker
import tech.stonks.goodnumberpicker.styles.GoodNumberPickerStyle

class LinesPickerOverlay : PickerOverlay {

    private var _style: GoodNumberPickerStyle = GoodNumberPickerStyle.default

    private var _paint: Paint = Paint().apply {
        strokeWidth = 2f
        color = _style.overlayColor
        style = Paint.Style.STROKE
    }

    override fun styleChanged(style: GoodNumberPickerStyle) {
        _style = style
        _paint.color = _style.overlayColor
    }

    override fun draw(canvas: Canvas, centerItemRect: Rect) {
        canvas.drawLine(
            0f,
            centerItemRect.top.toFloat(),
            canvas.width.toFloat(),
            centerItemRect.top.toFloat(),
            _paint
        )
        canvas.drawLine(
            0f,
            centerItemRect.bottom.toFloat(),
            canvas.width.toFloat(),
            centerItemRect.bottom.toFloat(),
            _paint
        )
    }
}