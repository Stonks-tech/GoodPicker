package tech.stonks.goodnumberpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import tech.stonks.goodnumberpicker.common.getRepeatableRange
import tech.stonks.goodnumberpicker.item.NumberPickerItem
import tech.stonks.goodnumberpicker.item.TextNumberPickerItem
import tech.stonks.goodnumberpicker.overlay.LinesPickerOverlay
import tech.stonks.goodnumberpicker.overlay.PickerOverlay

class GoodNumberPicker : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val _scrollHandler = ScrollHandler(context)

    var items: List<NumberPickerItem> = List(10) { TextNumberPickerItem(it.toString()) }
        set(value) {
            _allItemsHeight = calculateAllItemsHeight()
            field = value
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
        _scrollHandler.incrementYRange = value.bottom .. height
        _scrollHandler.decrementYRange = 0 .. value.top
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
        }
    }

    override fun onDetachedFromWindow() {
        _scrollHandler.setScrollListener { }
        super.onDetachedFromWindow()
    }

}