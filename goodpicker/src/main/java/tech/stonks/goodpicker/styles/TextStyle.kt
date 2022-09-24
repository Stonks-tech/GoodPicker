package tech.stonks.goodpicker.styles

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FontRes
import tech.stonks.goodpicker.R
import tech.stonks.goodpicker.common.getColorOrFetchFromResource
import tech.stonks.goodpicker.common.getDimensionOrFetchFromResource
import tech.stonks.goodpicker.common.getResourceIdOrNull

data class TextStyle(
    @FontRes
    val font: Int?,
    @ColorInt
    val textColor: Int,
    @Dimension
    val textSize: Float,
    //This will be applied only if API >= 28
    //If you want to apply various font weights on older android versions use separate font
    //resources for each weight
    val fontWeight: Int
) : CustomStyle {
    companion object {
        const val DEFAULT_FONT_WEIGHT = 400
        const val DEFAULT_TEXT_SIZE = 50f
        const val DEFAULT_TEXT_COLOR = Color.BLACK

        val default = TextStyle(
            font = null,
            textColor = DEFAULT_TEXT_COLOR,
            textSize = DEFAULT_TEXT_SIZE,
            fontWeight = DEFAULT_FONT_WEIGHT
        )

        fun fromTypedArray(typedArray: TypedArray, context: Context): TextStyle {
            return with(typedArray) {
                TextStyle(
                    font = getResourceIdOrNull(R.styleable.GoodNumberPicker_android_font),
                    textColor = getColorOrFetchFromResource(
                        context,
                        R.styleable.GoodNumberPicker_android_textColor,
                        DEFAULT_TEXT_COLOR
                    ),
                    textSize = getDimensionOrFetchFromResource(
                        context,
                        R.styleable.GoodNumberPicker_android_textSize,
                        DEFAULT_TEXT_SIZE
                    ),
                    fontWeight = getInteger(
                        R.styleable.GoodNumberPicker_android_textFontWeight,
                        DEFAULT_FONT_WEIGHT
                    )
                )
            }
        }
    }
}