package tech.stonks.goodnumberpicker.common

fun <T> List<T>.getRepeatableRange(start: Int, end: Int): List<T> {
    return (start..end).map {
        this[it.toRepeatableIndex(this.size)]
    }
}

fun Int.toRepeatableIndex(size: Int): Int {
    return if(this < 0) {
        size + this
    } else {
        this % size
    }
}