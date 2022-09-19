package tech.stonks.goodnumberpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FontRes
import tech.stonks.goodnumberpicker.GoodNumberPicker.TextStyle.Companion.DEFAULT_TEXT_COLOR
import tech.stonks.goodnumberpicker.common.getColorOrFetchFromResource
import tech.stonks.goodnumberpicker.common.getDimensionOrFetchFromResource
import tech.stonks.goodnumberpicker.common.getRepeatableRange
import tech.stonks.goodnumberpicker.common.getResourceIdOrNull
import tech.stonks.goodnumberpicker.common.obtainAttributes
import tech.stonks.goodnumberpicker.item.NumberPickerItem
import tech.stonks.goodnumberpicker.item.TextNumberPickerItem
import tech.stonks.goodnumberpicker.overlay.LinesPickerOverlay
import tech.stonks.goodnumberpicker.overlay.PickerOverlay
import kotlin.math.roundToInt

class GoodNumberPicker : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        fetchAttributes(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        fetchAttributes(attrs, defStyleAttr)
    }

    private var _style: Style = Style.default()

    private val _scrollHandler = ScrollHandler(context)

    private var _lastPublishedItem: Int = -1

    /**
     * If true updates are published only when scroll animation finishes
     * If false updates are published each time selected item changes (even during animating)
     */
    var publishUpdatesOnlyOnAnimationEnd: Boolean = true
    var items: List<NumberPickerItem> = List(10) {
        TextNumberPickerItem(
            context,
            it.toString(),
            _style
        )
    }
        set(value) {
            _allItemsHeight = calculateAllItemsHeight()
            field = value
        }

    var onSelectedPositionChanged: ((Int) -> Unit) = {}
    val selectedPosition: Int
        get() {
            val value = -((currentValue / _itemHeight.toFloat()).roundToInt() - 1)
            return if (value < 0) {
                items.size + value
            } else if (value >= items.size) {
                value - items.size
            } else {
                value
            }
        }
    var pickerOverlay: PickerOverlay = LinesPickerOverlay()

    var visibleItems: Int = 3
        set(value) {
            field = value
            _itemHeight = calculateItemHeight()
            invalidate()
        }
    private var _itemHeight: Int = calculateItemHeight()
    private var currentValue: Int = 0
    private var _centerItemRect: Rect = getCenterItemRect()
        set(value) {
            field = value
            _scrollHandler.incrementYRange = value.bottom..height
            _scrollHandler.decrementYRange = 0..value.top
        }
    private var _scrolledItems: Int = 0
    private var _allItemsHeight: Int = 0

    init {
        isScrollContainer = true
        setOnTouchListener(_scrollHandler)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        _itemHeight = calculateItemHeight()
        _scrollHandler.itemHeight = _itemHeight
        _centerItemRect = getCenterItemRect()
        _allItemsHeight = calculateAllItemsHeight()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        currentValue = _scrollHandler.currentValue % _allItemsHeight
        _scrolledItems = currentValue / _itemHeight
        val items = getItemsToDraw()
        for (index in items.indices) {
            items[index].draw(
                canvas,
                ((index - _scrolledItems) * _itemHeight.toFloat()) + currentValue,
                width,
                _itemHeight
            )
        }
        pickerOverlay.draw(canvas, _centerItemRect)
    }

    private fun getItemsToDraw(): List<NumberPickerItem> {
        val totalVisibleItems = visibleItems + 3
        val startIndex = -_scrolledItems
        val endIndex = startIndex + totalVisibleItems
        return items.getRepeatableRange(startIndex, endIndex)
    }

    private fun getCenterItemRect(): Rect {
        val centerItemIndex = visibleItems / 2
        val centerItemTop = centerItemIndex * _itemHeight
        val centerItemBottom = centerItemTop + _itemHeight
        return Rect(0, centerItemTop, width, centerItemBottom)
    }

    private fun calculateItemHeight(): Int {
        return (height / visibleItems)
    }

    private fun calculateAllItemsHeight(): Int {
        return items.size * _itemHeight
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _scrollHandler.setScrollListener {
            invalidate()
            if (!publishUpdatesOnlyOnAnimationEnd) {
                publishCurrentPosition()
            }
        }
        _scrollHandler.setOnAnimationEndListener {
            publishCurrentPosition()
        }
    }

    override fun onDetachedFromWindow() {
        _scrollHandler.setScrollListener { }
        _scrollHandler.setOnAnimationEndListener { }
        super.onDetachedFromWindow()
    }

    private fun publishCurrentPosition() {
        val newItem = selectedPosition
        if (_lastPublishedItem != newItem) {
            _lastPublishedItem = newItem
            onSelectedPositionChanged(newItem)
        }
    }

    private fun fetchAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        obtainAttributes(attrs, defStyleAttr) {
            _style = Style(
                getInteger(R.styleable.GoodNumberPicker_visibleItems, 3),
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
                        TextStyle.DEFAULT_TEXT_SIZE
                    ),
                    fontWeight = getInteger(
                        R.styleable.GoodNumberPicker_android_textFontWeight,
                        TextStyle.DEFAULT_FONT_WEIGHT
                    )
                )
            )
        }
        items.forEach {
            it.styleChanged(_style)
        }
        visibleItems = _style.visibleItems
    }

    data class Style(
        val visibleItems: Int,
        val textStyle: TextStyle
    ) {
        companion object {
            fun default() = Style(
                visibleItems = 3,
                textStyle = TextStyle.default
            )
        }
    }

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
    ) {
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
        }
    }
}