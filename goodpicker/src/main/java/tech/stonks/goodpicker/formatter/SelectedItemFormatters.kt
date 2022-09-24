package tech.stonks.goodpicker.formatter

import android.content.res.ColorStateList
import tech.stonks.goodpicker.ItemFormatter
import tech.stonks.goodpicker.item.PickerItem
import tech.stonks.goodpicker.styles.DrawableStyle
import tech.stonks.goodpicker.styles.TextStyle

class SelectedDrawableItemFormatter(
    override val color: ColorStateList,
    override val isSelected: (position: Int) -> Boolean
): BaseColorSelectedItemFormatter() {
    override fun PickerItem.applyColor(color: Int) {
        styleChanged(
            style.modifyCustomStyle<DrawableStyle> {
                this.copy(
                    tint = color
                )
            }
        )
    }
}

class SelectedTextItemFormatter(
    override val color: ColorStateList,
    override val isSelected: (position: Int) -> Boolean
): BaseColorSelectedItemFormatter() {
    override fun PickerItem.applyColor(color: Int) {
        styleChanged(
            style.modifyCustomStyle<TextStyle> {
                this.copy(
                    textColor = color
                )
            }
        )
    }
}

abstract class BaseColorSelectedItemFormatter: BaseSelectedItemFormatter() {
    abstract val color: ColorStateList

    abstract fun PickerItem.applyColor(color: Int)

    override fun invoke(position: Int, item: PickerItem): PickerItem {
        return item.apply {
            val color = if (isSelected(position)) {
                color.getColorForState(
                    intArrayOf(android.R.attr.state_selected),
                    TextStyle.DEFAULT_TEXT_COLOR
                )
            } else {
                color.defaultColor
            }
            applyColor(color)
        }
    }
}

abstract class BaseSelectedItemFormatter: ItemFormatter{
    abstract val isSelected: (position: Int) -> Boolean
}