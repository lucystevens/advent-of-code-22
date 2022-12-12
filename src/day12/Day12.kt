package day12

import common.Challenge
import common.Part
import common.mapping.Grid
import common.mapping.SteppedPathFinder
import common.tests
import toGrid

class Day12 : Challenge<Grid<Char>, Long>() {
    override val day: String = "day12"

    private val Grid<Char>.start
        get() = map.toList().find { it.second == 'S' }!!.first

    private val Grid<Char>.end
        get() = map.toList().find { it.second == 'E' }!!.first

    private val Grid<Char>.pathFinder
        get() = SteppedPathFinder(end, this) { from, to ->
            from.value.toHeight() - to.value.toHeight() >= -1
        }

    override fun part1(input: Grid<Char>): Long {
        return input.pathFinder.findShortestPath(input.start).size.toLong()
    }

    override fun part2(input: Grid<Char>): Long {
        val pathFinder = input.pathFinder
        return input.map.toList()
            .filter { (_, c) -> c.toHeight() == 'a'.code }
            .map { it.first }
            .minOf { pathFinder.findShortestDistance(it) }
    }

    override fun parseInput(lines: List<String>): Grid<Char> {
        return lines.map { it.toCharArray().toList() }.toGrid()
    }

    override val tests = tests<Long> {
        file("test", part1 = 31L, part2 = 29)
        file("input", part1 = 339, part2 = 332)
    }

}

fun Char.toHeight() =
    when(this){
        'S' -> 'a'.code
        'E' -> 'z'.code
        else -> code
    }

fun main() {
    Day12().test(Part.BOTH)
}
