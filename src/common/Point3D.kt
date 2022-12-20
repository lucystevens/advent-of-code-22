package common

import kotlin.math.abs

data class Point3D(var x: Int, var y: Int, var z: Int){

    fun transform(delta: Point3D): Point3D{
        return transform(delta.x, delta.y, delta.z)
    }

    fun transform(xDelta: Int = 0, yDelta: Int = 0, zDelta: Int = 0): Point3D{
        return Point3D(x + xDelta, y+ yDelta, z+zDelta)
    }

    fun setAxis(a: Char, value: Int) = when(a) {
        'x' -> x = value
        'y' -> y = value
        'z' -> z = value
        else -> throw IllegalArgumentException("Axis $a not recognised")
    }

    fun getAxis(a: Char) = when(a) {
        'x' -> x
        'y' -> y
        'z' -> z
        else -> throw IllegalArgumentException("Axis $a not recognised")
    }

    fun isAdjacent(other: Point3D): Boolean =
        (x == other.x && y == other.y && abs(z-other.z) == 1) ||
        (x == other.x && abs(y-other.y) == 1 && z == other.z) ||
        (abs(x-other.x) == 1 && y == other.y && z == other.z)

    // does not include diagonals
    fun adjacentPoints() = listOf(
        transform(xDelta = -1),
        transform(xDelta = 1),
        transform(yDelta = -1),
        transform(yDelta = 1),
        transform(zDelta = -1),
        transform(zDelta = 1)
    )
}