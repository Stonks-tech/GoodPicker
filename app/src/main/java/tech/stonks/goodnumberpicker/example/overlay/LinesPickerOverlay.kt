package tech.stonks.goodnumberpicker.example.overlay

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class LinesPickerOverlay : PickerOverlay {

    private var _paint: Paint = Paint().apply {
        strokeWidth = 2f
        color = Color.BLACK
        style = Paint.Style.STROKE
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