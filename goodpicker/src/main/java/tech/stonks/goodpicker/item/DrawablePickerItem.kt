package tech.stonks.goodpicker.item

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import tech.stonks.goodpicker.styles.DrawableStyle
import tech.stonks.goodpicker.styles.GoodPickerStyle

class DrawablePickerItem(val drawable: Drawable) : PickerItem {
    private var _style: GoodPickerStyle = GoodPickerStyle.default
    override val style: GoodPickerStyle
        get() = _style
    private val _drawableStyle: DrawableStyle
        get() {
            return _style.getCustomStyle() ?: throw IllegalStateException("No drawable style found")
        }

    override fun draw(canvas: Canvas, y: Float, width: Int, height: Int) {
        val style = _drawableStyle
        canvas.save()
        canvas.translate(
            (width - style.size) / 2f,
            y + ((height - style.size) / 2f),
        )
        drawable.setTint(style.tint)
        drawable.setBounds(0, 0, style.size, style.size)
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun styleChanged(style: GoodPickerStyle) {
        _style = style
    }

    companion object {
        fun fromResource(context: Context, resId: Int): DrawablePickerItem {
            return DrawablePickerItem(
                ContextCompat.getDrawable(context, resId)!!
            )
        }
    }
}