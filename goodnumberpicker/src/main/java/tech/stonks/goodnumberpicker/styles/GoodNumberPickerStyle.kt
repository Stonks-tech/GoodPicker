package tech.stonks.goodnumberpicker.styles

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.annotation.ColorInt
import tech.stonks.goodnumberpicker.R
import tech.stonks.goodnumberpicker.common.getColorOrFetchFromResource

data class GoodNumberPickerStyle(
    val visibleItems: Int,
    @ColorInt
    val overlayColor: Int,
    val customStyles: List<CustomStyle>
) {

    inline fun <reified T : CustomStyle> modifyCustomStyle(crossinline body: T.() -> T): GoodNumberPickerStyle {
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

        val default = GoodNumberPickerStyle(
            visibleItems = DEFAULT_VISIBLE_ITEMS,
            overlayColor = DEFAULT_OVERLAY_COLOR,
            customStyles = listOf(TextStyle.default, DrawableStyle.default)
        )

        fun fromTypedArray(typedArray: TypedArray, context: Context): GoodNumberPickerStyle {
            return with(typedArray) {
                GoodNumberPickerStyle(
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