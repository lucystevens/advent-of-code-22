package common

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    fun above(): Point = Point(x, y-1)
    fun below(): Point = Point(x, y+1)
    fun left(): Point = Point(x-1, y)
    fun right(): Point = Point(x+1, y)

    fun topLeft(): Point = above().left()
    fun topRight(): Point = above().right()
    fun bottomLeft(): Point = below().left()
    fun bottomRight(): Point = below().right()

    fun transform(xDelta: Int, yDelta: Int): Point{
        return Point(x + xDelta, y+ yDelta)
    }

    fun move(direction: String, steps: Int = 1): Point = when(direction){
        "L" -> left()
        "R" -> right()
        "U" -> above()
        "D" -> below()
        else -> error("Unrecognised direction $direction")
    }

    fun distanceTo(point: Point): Vector =
        Vector(abs(x - point.x), abs(y - point.y))

    // always includes diagonals
    fun isAdjacent(point: Point) =
        abs(x - point.x) <= 1 && abs(y - point.y) <= 1

    fun adjacentPoints(includeDiagonals: Boolean = false): List<Point> =
        when(includeDiagonals){
            true -> IntRange(-1,1).flatMap {
                        xMod -> IntRange(-1,1).map {
                        yMod -> Point(x+xMod, y+yMod)
                    }
            }.filterNot { it == this }
            false -> listOf(above(), below(), left(), right())
        }
}
