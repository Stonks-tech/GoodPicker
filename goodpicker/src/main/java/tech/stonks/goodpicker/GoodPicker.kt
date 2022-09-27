package tech.stonks.goodpicker

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import tech.stonks.goodpicker.common.getColorStateListOrFetchFromResource
import tech.stonks.goodpicker.common.getRepeatableRange
import tech.stonks.goodpicker.common.obtainAttributes
import tech.stonks.goodpicker.common.toRepeatableIndex
import tech.stonks.goodpicker.formatter.SelectedTextItemFormatter
import tech.stonks.goodpicker.item.PickerItem
import tech.stonks.goodpicker.item.TextPickerItem
import tech.stonks.goodpicker.overlay.LinesPickerOverlay
import tech.stonks.goodpicker.overlay.PickerOverlay
import tech.stonks.goodpicker.styles.GoodPickerStyle
import tech.stonks.goodpicker.styles.TextStyle.Companion.DEFAULT_TEXT_COLOR
import kotlin.math.roundToInt

open class GoodPicker : View {

    companion object {
        //How many items will be added offscreen on each side
        const val OFFSCREEN_ITEMS = 1
    }

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

    /** Style of the picker, see `GoodNumberPicker.Style`. Basically it contains all the fields that are available in xml */
    var style: GoodPickerStyle = GoodPickerStyle.default
        set(value) {
            field = value
            publishChangedStyle()
            invalidate()
        }

    /** Formatter that will be used to transform items */
    var itemFormatter: ItemFormatter = { _, item -> item }

    /**
     * If true updates are published only when scroll animation finishes
     * If false updates are published each time selected item changes (even during animating)
     */
    var publishUpdatesOnlyOnAnimationEnd: Boolean = true

    /** List of items that will be displayed in the picker. */
    var items: List<PickerItem> = List(10) {
        TextPickerItem(
            context,
            it.toString(),
            style
        )
    }
        set(value) {
            _allItemsHeight = calculateAllItemsHeight()
            field = value
        }

    /** Listener that will be called when selected position changes. By default, it will be called when user stops scrolling.
     * This behaviour might be changed with `publishUpdatesOnlyOnAnimationEnd` property
     */
    var onSelectedPositionChanged: ((Int) -> Unit) = {}

    /** You can set it to change selected position programmatically. */
    var selectedPosition: Int
        set(value) {
            _scrollHandler.moveBy(selectedPosition - value)
        }
        get() {
            return drawPositionToAbsolutePosition(((visibleItems - 1) / 2))
        }

    /** Overlay that will be drawn above picked values. By default it will display two lines */
    var pickerOverlay: PickerOverlay = LinesPickerOverlay()
        set(value) {
            field = value
            invalidate()
        }

    /** Number of visible items in the picker */
    var visibleItems: Int = 3
        set(value) {
            if (value % 2 == 0) {
                throw IllegalArgumentException("Visible items must be odd")
            }
            field = value
            _itemHeight = calculateItemHeight()
            invalidate()
        }

    private val _scrollHandler = ScrollHandler(context)

    private var _lastPublishedItem: Int = -1

    private var _itemHeight: Int = calculateItemHeight()
    private var _currentValue: Int = 0
    private var _centerItemRect: Rect = getCenterItemRect()
        set(value) {
            field = value
            _scrollHandler.incrementYRange = value.bottom..height
            _scrollHandler.decrementYRange = 0..value.top
        }
    private var _scrolledItems: Int = 0
    private var _allItemsHeight: Int = 0

    private var _textColorStateList: ColorStateList = ColorStateList.valueOf(DEFAULT_TEXT_COLOR)
        set(value) {
            field = value
            itemFormatter = SelectedTextItemFormatter(value) {
                it == selectedPosition
            }
        }

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
        _currentValue = _scrollHandler.currentValue % _allItemsHeight
        _scrolledItems = _currentValue / _itemHeight
        val itemRange = getItemsRangeToDraw()
        val items = items.getRepeatableRange(itemRange.first, itemRange.last)
        for (index in items.indices) {
            itemFormatter(
                (itemRange.first + index).toRepeatableIndex(this.items.size),
                items[index]
            ).draw(
                canvas,
                ((index - _scrolledItems - OFFSCREEN_ITEMS) * _itemHeight.toFloat()) + _currentValue,
                width,
                _itemHeight
            )
        }
        pickerOverlay.draw(canvas, _centerItemRect)
    }

    private fun getItemsRangeToDraw(): IntRange {
        val totalVisibleItems = visibleItems + (OFFSCREEN_ITEMS * 2)
        val startIndex = -_scrolledItems - OFFSCREEN_ITEMS
        val endIndex = startIndex + totalVisibleItems
        return startIndex..endIndex
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
            _textColorStateList = getColorStateListOrFetchFromResource(
                context,
                R.styleable.GoodNumberPicker_android_textColor,
                DEFAULT_TEXT_COLOR
            )
            style = GoodPickerStyle.fromTypedArray(this, context)
        }
        publishChangedStyle()
    }

    private fun drawPositionToAbsolutePosition(index: Int): Int = try {
        val value = -((_currentValue / _itemHeight.toFloat()).roundToInt() - index)
        if (value < 0) {
            items.size + value
        } else if (value >= items.size) {
            value - items.size
        } else {
            value
        }
    } catch (ex: Exception) {
        0
    }

    private fun publishChangedStyle() {
        items.forEach {
            it.styleChanged(style)
        }
        pickerOverlay.styleChanged(style)
        visibleItems = style.visibleItems
    }

}