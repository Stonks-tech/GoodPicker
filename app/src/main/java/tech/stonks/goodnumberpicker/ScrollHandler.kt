package tech.stonks.goodnumberpicker

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import kotlin.math.roundToInt

class ScrollHandler(private val _context: Context) : View.OnTouchListener {
    private var _scrollListener: ScrollListener = {}
    private val _scroller: ValueAnimator = ValueAnimator().apply {
        duration = 300
        interpolator = DecelerateInterpolator(2.5f)
        addUpdateListener {
            currentValue = it.animatedValue as Int
            _scrollListener(currentValue)
        }
    }

    var currentValue: Int = 0
    private set

    /**
     * Final Scroll value will be rounded to value divisible by this number
     */
    var roundToValue: Int = 1
    private var _previousY: Int = 0

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                _previousY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val delta = event.y - _previousY
                _previousY = event.y.toInt()
                currentValue += delta.toInt()
                _scrollListener(currentValue)
            }
            MotionEvent.ACTION_UP -> {
                _previousY = 0
                _scroller.cancel()
                _scroller.setIntValues(
                    currentValue,
                    currentValue.roundToNearest(roundToValue)

                )
                _scroller.start()
            }
        }
        return true
    }

    private fun Int.roundToNearest(roundTo: Int): Int {
        return ((this / roundTo.toFloat()).roundToInt() * roundTo)
    }

    fun setScrollListener(scrollListener: ScrollListener) {
        this._scrollListener = scrollListener
    }
}

typealias ScrollListener = (y: Int) -> Unit