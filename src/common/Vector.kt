package common

import kotlin.math.abs

// basically the same as Point, but they represent different things
data class Vector(val x: Int, val y: Int) {
    fun manhattan() =
        abs(x) + abs(y)
}