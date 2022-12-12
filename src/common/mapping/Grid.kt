package common.mapping

import common.Point
import minMaxOf

class Grid<T>(
    val map: Map<Point, T> = mapOf()
) {

    fun getValue(point: Point): GridValue<T> =
        GridValue(point, map[point]!!)

    fun get(point: Point): T? =
        map[point]

    fun get(x: Int, y: Int): T? =
        get(Point(x,y))

    fun set(point: Point, item: T): Grid<T> =
        Grid(map.toMutableMap().apply {
            set(point, item)
        })

    fun set(x: Int, y: Int, item: T): Grid<T> =
        set(Point(x,y), item)

    fun printGrid(defaultVal: T){
        map.keys.minMaxOf { it.y }.let { it.first..it.second }.forEach { y ->
            map.keys.minMaxOf { it.x }.let { it.first..it.second }.forEach { x ->
                print(get(x,y) ?: defaultVal)
            }
            println()
        }

    }
}
