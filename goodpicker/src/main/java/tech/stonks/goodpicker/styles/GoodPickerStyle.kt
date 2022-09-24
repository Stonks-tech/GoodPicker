package tech.stonks.goodpicker.styles

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.annotation.ColorInt
import tech.stonks.goodpicker.R
import tech.stonks.goodpicker.common.getColorOrFetchFromResource

data class GoodPickerStyle(
    val visibleItems: Int,
    @ColorInt
    val overlayColor: Int,
    val customStyles: List<CustomStyle>
) {

    inline fun <reified T : CustomStyle> modifyCustomStyle(crossinline body: T.() -> T): GoodPickerStyle {
        return copy(customStyles = customStyles.map {
            if (it is T) {
                it.body()
            } else {
                it
            }
        })
    }

    inline fun <reified T : CustomStyle> getCustomStyle(): T? {
        return customStyles.find { it is T } as? T
    }

    companion object {
        const val DEFAULT_VISIBLE_ITEMS = 3
        const val DEFAULT_OVERLAY_COLOR = Color.BLACK

        val default = GoodPickerStyle(
            visibleItems = DEFAULT_VISIBLE_ITEMS,
            overlayColor = DEFAULT_OVERLAY_COLOR,
            customStyles = listOf(TextStyle.default, DrawableStyle.default)
        )

        fun fromTypedArray(typedArray: TypedArray, context: Context): GoodPickerStyle {
            return with(typedArray) {
                GoodPickerStyle(
                    visibleItems = getInteger(
                        R.styleable.GoodNumberPicker_visibleItems,
                        DEFAULT_VISIBLE_ITEMS
                    ),
                    overlayColor = getColorOrFetchFromResource(
                        context,
                        R.styleable.GoodNumberPicker_overlayColor,
                        DEFAULT_OVERLAY_COLOR
                    ),
                    customStyles = listOf(
                        TextStyle.fromTypedArray(typedArray, context),
                        DrawableStyle.fromTypedArray(typedArray, context),
                    )
                )
            }
        }
    }
}