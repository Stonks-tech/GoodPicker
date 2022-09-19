package tech.stonks.goodnumberpicker.example.common

fun <T> List<T>.getRepeatableRange(start: Int, end: Int): List<T> {
    return (start..end).map {
        if(it < 0) {
            this[this.size + it]
        } else {
            this[it % this.size]
        }
    }
}