package day18

import ReturnType
import common.Challenge
import common.Part
import common.Point3D
import common.tests
import minMaxOf

class Day18 : Challenge<List<Point3D>, ReturnType>() {
    override val day: String = "day18"

    override fun part1(input: List<Point3D>): ReturnType {
        return input.sumOf { point ->
            6 - input.count { it.isAdjacent(point) }
        }.toLong()
    }

    // minimum & maximum points
    fun List<Point3D>.minMax(): Pair<Int, Int>{
        val minMax = listOf(
            minMaxOf { it.x },
            minMaxOf { it.y },
            minMaxOf { it.z }
        )
        return minMax.minOf { it.first } to minMax.maxOf { it.second }
    }

    // breadth first search to find all points immediately outside the 'rock'
    override fun part2(input: List<Point3D>): ReturnType {
        // get min max points, then add 2 as a buffer
        val (min,max) = input.minMax()
            .let { (min,max) -> min-2 to max+2 }
        // setup sets
        val rocks = input.toSet()
        val visited = mutableSetOf<Point3D>()
        // start in minimum/maximum adjacent corners
        var current = setOf(
            Point3D(min+1, min+1, min+1),
            Point3D(max-1, max-1, max-1))
        // BFS of outer space
        while(current.isNotEmpty()){
            visited += current
            // get every adjacent point to the current points
            current = current.flatMap { it.adjacentPoints() }
                    // bound search to the minimum/maximum box
                .filter {
                    it.x > min && it.y > min && it.z > min &&
                    it.x < max && it.y < max && it.z < max
                }
                    // where not visited, and not in rock
                .filter { !rocks.contains(it) && !visited.contains(it) }
                .toSet()
        }
        // find adjacent rock faces to the outside space
        return input.sumOf { point ->
            visited.count { it.isAdjacent(point) }
        }.toLong()
    }

    override fun parseInput(lines: List<String>): List<Point3D> {
        return lines.map { it.toPoint3D() }
    }

    override val tests = tests<ReturnType> {
        file("test", part1 = 64, part2 = 58)
        file("input", part1 = 4332, part2 = 2524)
    }

}

fun String.toPoint3D(): Point3D = 
    split(",")
        .map { it.toInt() }
        .let { Point3D(it[0],it[1],it[2]) }

fun main() {
    Day18().test(Part.BOTH)
}
