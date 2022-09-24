package tech.stonks.goodnumberpicker.formatter

import android.content.res.ColorStateList
import tech.stonks.goodnumberpicker.ItemFormatter
import tech.stonks.goodnumberpicker.item.NumberPickerItem
import tech.stonks.goodnumberpicker.styles.DrawableStyle
import tech.stonks.goodnumberpicker.styles.TextStyle

class SelectedDrawableItemFormatter(
    override val color: ColorStateList,
    override val isSelected: (position: Int) -> Boolean
): BaseColorSelectedItemFormatter() {
    override fun NumberPickerItem.applyColor(color: Int) {
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
    override fun NumberPickerItem.applyColor(color: Int) {
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

    abstract fun NumberPickerItem.applyColor(color: Int)

    override fun invoke(position: Int, item: NumberPickerItem): NumberPickerItem {
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