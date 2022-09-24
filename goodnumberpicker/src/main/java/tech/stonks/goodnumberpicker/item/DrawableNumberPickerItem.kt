package tech.stonks.goodnumberpicker.item

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import tech.stonks.goodnumberpicker.styles.DrawableStyle
import tech.stonks.goodnumberpicker.styles.GoodNumberPickerStyle

class DrawableNumberPickerItem(val drawable: Drawable) : NumberPickerItem {
    private var _style: GoodNumberPickerStyle = GoodNumberPickerStyle.default
    override val style: GoodNumberPickerStyle
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

    override fun styleChanged(style: GoodNumberPickerStyle) {
        _style = style
    }

    companion object {
        fun fromResource(context: Context, resId: Int): DrawableNumberPickerItem {
            return DrawableNumberPickerItem(
                ContextCompat.getDrawable(context, resId)!!
            )
        }
    }
}