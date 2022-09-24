package tech.stonks.goodpicker.styles

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.annotation.ColorInt
import tech.stonks.goodpicker.R
import tech.stonks.goodpicker.common.getColorOrFetchFromResource
import tech.stonks.goodpicker.common.getDimensionPixelSizeOrFetchFromResource

data class DrawableStyle(
    @ColorInt
    val tint: Int,
    val size: Int
) : CustomStyle {
    companion object {
        const val DEFAULT_TINT = Color.BLACK
        const val DEFAULT_SIZE = 50

        val default = DrawableStyle(
            tint = DEFAULT_TINT,
            size = DEFAULT_SIZE
        )

        fun fromTypedArray(typedArray: TypedArray, context: Context): DrawableStyle {
            return with(typedArray) {
                DrawableStyle(
                    tint = getColorOrFetchFromResource(
                        context,
                        R.styleable.GoodNumberPicker_android_tint,
                        DEFAULT_TINT
                    ),
                    size = getDimensionPixelSizeOrFetchFromResource(
                        context,
                        R.styleable.GoodNumberPicker_size,
                        DEFAULT_SIZE
                    )
                )
            }
        }
    }
}