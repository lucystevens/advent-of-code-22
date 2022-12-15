package day14

import combine
import common.Challenge
import common.Part
import common.Point
import common.tests
import mapWithLast
import minMaxOf
import range
import toPoint

class Day14 : Challenge<List<List<Point>>, Int>() {
    override val day: String = "day14"

    val entry: Point = Point(500,0)
    fun List<List<Point>>.flattenPaths(): List<Point> =
        flatMap {
            it.mapWithLast { last, current ->
                last.drawLine(current)
            }
        }.flatten()

    fun simulateSand(rocks: Set<Point>, grains: Set<Point>): Point?{
        val gridBottom = rocks.maxOf { it.y }
        var sand = entry
        while(sand.y < gridBottom){
            val sandPoints = listOf(sand.below(), sand.bottomLeft(), sand.bottomRight())
            val nextPoint = sandPoints.firstOrNull {
                !rocks.contains(it) && !grains.contains(it)
            }
            if(nextPoint == null) return sand
            else  sand = nextPoint
        }
        return null
    }

    override fun part1(input: List<List<Point>>): Int {
        val rocks = input
            .flattenPaths()
            .toSet()
        val grains = mutableSetOf<Point>()

        var sand = simulateSand(rocks, grains)
        while(sand != null){
            grains += sand
            sand = simulateSand(rocks, grains)
        }

        return grains.size
    }

    fun Set<Point>.addFloor(): Set<Point> {
        val floorY = maxOf { it.y } + 2
        val (minX, maxX) = minMaxOf { it.x }
        return this + (minX-1000..maxX+1000).map { x ->
            Point(x,floorY)
        }.toSet()
    }

    override fun part2(input: List<List<Point>>): Int {
        val rocks = input
            .flattenPaths()
            .toSet()
            .addFloor()
        val grains = mutableSetOf<Point>()

        var sand = simulateSand(rocks, grains)
        while(sand != null && sand != entry){
            grains += sand
            sand = simulateSand(rocks, grains)
        }

        if(sand == null) error("Sand fell through the floor!")
        return grains.size+1
    }

    override fun parseInput(lines: List<String>): List<List<Point>> {
        return lines.map { line ->
            line.split(" -> ").map { it.toPoint() }
        }
    }

    override val tests = tests<Int> {
        file("test", part1 = 24, part2 = 93)
        file("input", part1 = 913, part2 = 30762)
    }

}

fun Point.drawLine(to: Point) =
    range(x,to.x).combine(range(y,to.y)){ x, y ->
        Point(x,y)
    }


fun main() {
    listOf(" ", "x")
    Day14().test(Part.BOTH)
}
