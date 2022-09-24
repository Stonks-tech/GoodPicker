package tech.stonks.goodpicker.overlay

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import tech.stonks.goodpicker.styles.GoodPickerStyle

class DrawablePickerOverlay(
    private val _topDrawable: Drawable,
    private val _bottomDrawable: Drawable
) : PickerOverlay {
    private var _style: GoodPickerStyle = GoodPickerStyle.default
    override fun styleChanged(style: GoodPickerStyle) {
        _style = style
    }

    override fun draw(canvas: Canvas, centerItemRect: Rect) {
        _topDrawable.setBounds(
            0,
            0,
            canvas.width,
            centerItemRect.top
        )
        _topDrawable.draw(canvas)
        _bottomDrawable.setBounds(
            0,
            centerItemRect.bottom,
            canvas.width,
            canvas.height
        )
        _bottomDrawable.draw(canvas)
    }

    companion object {
        fun symmetric(topDrawable: Drawable): DrawablePickerOverlay {
            return DrawablePickerOverlay(topDrawable, RotateDrawable().apply {
                drawable = topDrawable.constantState?.newDrawable()!!.mutate()
                this.fromDegrees = 180f
            })
        }
    }
}