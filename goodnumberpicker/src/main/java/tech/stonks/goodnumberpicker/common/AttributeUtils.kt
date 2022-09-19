package tech.stonks.goodnumberpicker.common

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import tech.stonks.goodnumberpicker.R

fun View.obtainAttributes(attrs: AttributeSet?, defStyleAttr: Int, block: TypedArray.() -> Unit) {
    context.obtainStyledAttributes(attrs, R.styleable.GoodNumberPicker, defStyleAttr, 0).let {
        it.block()
        it.recycle()
    }
}

fun TypedArray.getResourceIdOrNull(index: Int): Int? {
    return if (hasValue(index)) getResourceId(index, 0) else null
}

fun TypedArray.getColorOrFetchFromResource(
    context: Context,
    index: Int,
    @ColorInt defValue: Int
): Int {
    return if (hasValue(index)) getColor(index, defValue) else try {
        ContextCompat.getColor(
            context, getResourceIdOrNull(
                index
            )!!
        )
    } catch (ex: Exception) {
        defValue
    }
}

fun TypedArray.getDimensionOrFetchFromResource(
    context: Context,
    index: Int,
    defValue: Float
): Float {
    return if (hasValue(index))
        getDimension(index, defValue)
    else try {
        context.resources.getDimension(
            getResourceIdOrNull(index)!!
        )
    } catch (ex: Exception) {
        defValue
    }
}