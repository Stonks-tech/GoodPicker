package tech.stonks.goodnumberpicker

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import tech.stonks.goodnumberpicker.common.getRepeatableRange
import tech.stonks.goodnumberpicker.item.NumberPickerItem
import tech.stonks.goodnumberpicker.item.TextNumberPickerItem
import tech.stonks.goodnumberpicker.overlay.LinesPickerOverlay
import tech.stonks.goodnumberpicker.overlay.PickerOverlay
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class GoodNumberPicker : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var _animator: ValueAnimator = ValueAnimator.ofInt(0, 100).apply {
        duration = 600
        addUpdateListener {
            invalidate()
        }
    }

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
    private var _scrolledItems: Int = 0
    private var _allItemsHeight: Int = 0


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        _itemHeight = calculateItemHeight()
        _centerItemRect = getCenterItemRect()
        _allItemsHeight = calculateAllItemsHeight()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        currentValue = (_animator.animatedValue as Int) % _allItemsHeight
        _scrolledItems = currentValue / _itemHeight
        val items = getItemsToDraw()
        println("items: $items, currentValue: $currentValue, _scrolledItems: $_scrolledItems")
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

    var actionDownTime: Long = 0
    var actionDownY: Long = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDownTime = System.currentTimeMillis()
                actionDownY = event.y.toLong()
                true
            }
            MotionEvent.ACTION_UP -> {
                val diffY = event.y - actionDownY
                val diffTime = System.currentTimeMillis() - actionDownTime
                if ((diffY > 50 || diffY < -50) && diffTime > 100) {
                    var endValue: Int = currentValue + diffY.toInt()
                    endValue = (endValue / _itemHeight.toFloat()).roundToInt() * _itemHeight
                    _animator.setIntValues(currentValue, endValue)
                    _animator.start()
                }
                true
            }
            else -> super.onTouchEvent(event)
        }
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
}