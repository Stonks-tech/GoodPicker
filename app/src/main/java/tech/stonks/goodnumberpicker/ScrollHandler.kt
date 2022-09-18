package tech.stonks.goodnumberpicker

import android.animation.ValueAnimator
import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import kotlin.math.abs
import kotlin.math.roundToInt

class ScrollHandler(private val _context: Context) : View.OnTouchListener {
    companion object {
        private const val VELOCITY_THRESHOLD = 200
        private const val ROUND_DURATION = 300
    }

    private var _scrollListener: ScrollListener = {}
    private val _flingInterpolator = OvershootInterpolator(1f)
    private val _roundInterpolator = DecelerateInterpolator(2.5f)
    private val _scrollAnimator: ValueAnimator = ValueAnimator().apply {
        addUpdateListener {
            currentValue = it.animatedValue as Int
            _scrollListener(currentValue)
        }
    }
    private lateinit var _velocityTracker: VelocityTracker

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
                _velocityTracker = VelocityTracker.obtain()
                _velocityTracker.addMovement(event)
                _previousY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                _velocityTracker.addMovement(event)
                val delta = event.y - _previousY
                _previousY = event.y.toInt()
                currentValue += delta.toInt()
                _scrollListener(currentValue)
            }
            MotionEvent.ACTION_UP -> {
                _velocityTracker.addMovement(event)
                _previousY = 0
                _velocityTracker.computeCurrentVelocity(100)
                println("Velocity: ${_velocityTracker.yVelocity}")
                if(abs(_velocityTracker.yVelocity) > VELOCITY_THRESHOLD) {
                    fling(_velocityTracker.yVelocity.toInt())
                }else{
                    roundToNearestItem()
                }
                _velocityTracker.recycle()
            }
        }
        return true
    }

    private fun fling(velocity: Int) {
        _scrollAnimator.cancel()
        _scrollAnimator.interpolator = _flingInterpolator
        _scrollAnimator.duration = (abs(velocity)).toLong()
        _scrollAnimator.setIntValues(currentValue, (currentValue + velocity).roundToNearest(roundToValue))
        _scrollAnimator.start()
    }

    private fun roundToNearestItem() {
        _scrollAnimator.cancel()
        _scrollAnimator.interpolator = _roundInterpolator
        _scrollAnimator.duration = ROUND_DURATION.toLong()
        _scrollAnimator.setIntValues(
            currentValue,
            currentValue.roundToNearest(roundToValue)
        )
        _scrollAnimator.start()

    }

    private fun Int.roundToNearest(roundTo: Int): Int {
        return ((this / roundTo.toFloat()).roundToInt() * roundTo)
    }

    fun setScrollListener(scrollListener: ScrollListener) {
        this._scrollListener = scrollListener
    }
}

typealias ScrollListener = (y: Int) -> Unit