package day08

import common.Challenge
import common.Part
import common.Point
import common.tests
import kotlin.math.max

class Day08 : Challenge<List<List<Int>>, Int>() {
    override val day: String = "day08"

    override fun part1(input: List<List<Int>>): Int {
        val visiblePoints = mutableSetOf<Point>()
        val height = input.size-1
        val width = input[0].size-1
        // from left, then right
        input.forEachIndexed { y, it ->
            visiblePoints.addAll(it.findVisible { x ->
                Point(x, y)
            })
            visiblePoints.addAll(it.reversed().findVisible { x ->
                Point(width-x, y)
            })
        }

        // from top, then bottom
        input.pivot().forEachIndexed { x, it ->
            visiblePoints.addAll(it.findVisible { y ->
                Point(x, y)
            })
            visiblePoints.addAll(it.reversed().findVisible { y ->
                Point(x, height-y)
            })
        }

        return visiblePoints.size
    }

    fun List<Int>.findVisible(toPoint: (Int) -> Point): List<Point> {
        var maxHeight = -1
        return mapIndexedNotNull { index, tree ->
            val res = if(tree > maxHeight) toPoint(index) else null
            maxHeight = max(tree, maxHeight)
            res
        }
    }

    override fun part2(input: List<List<Int>>): Int {
        val height = input.size-1
        val width = input[0].size-1
        // ignore trees on edges as they will have a score of 0
        return (1 until height).flatMap { y ->
            (1 until width).map { x ->
                Point(x, y)
            }
        }.maxOf { it.scenicScore(input) }
    }

    fun Point.scenicScore(map: List<List<Int>>) =
        scenicScore(map){ left() } *
                scenicScore(map){ above() } *
                scenicScore(map){ below() } *
                scenicScore(map){ right() }

    fun Point.scenicScore(map: List<List<Int>>, direction: Point.() -> Point): Int{
        var score = 0
        val thisTree = map.get(x, y)!!
        var point = direction()
        var tree = map.get(point.x, point.y) ?: 10
        while(tree < thisTree){
            score++
            point = point.direction()
            tree = map.get(point.x, point.y) ?: 10
        }
        if(tree != 10) score++
        return score
    }

    override fun parseInput(lines: List<String>): List<List<Int>> {
        return lines.map { it.toCharArray().map(Char::digitToInt) }
    }

    override val tests = tests<Int> {
        file("test", part1 = 21, part2 = 8)
        file("input", part1 = 1676, part2 = 313200)
    }

}

fun <T> List<List<T>>.get(x: Int, y: Int): T? =
    getOrNull(y)?.getOrNull(x)


// convert from list of rows to list of columns
fun <T> List<List<T>>.pivot() =
    List(size) { index -> column(index) }

fun <T> List<List<T>>.column(index: Int) =
    map { it[index] }


fun main() {
    Day08().test(Part.BOTH)
}
