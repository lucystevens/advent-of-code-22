package day04

import common.Challenge
import mapBoth
import toPair

typealias Range = Pair<Long,Long>

class Day04 : Challenge<List<Pair<Range,Range>>, Long>() {
    override val day: String = "day04"

    override fun part1(input: List<Pair<Range,Range>>): Long {
        return input.filter {
            it.first.contains(it.second) || it.second.contains(it.first)
        }.size.toLong()
    }

    override fun part2(input: List<Pair<Range,Range>>): Long {
        return input.filter {
            it.first.overlap(it.second) || it.second.overlap(it.first)
        }.size.toLong()
    }

    override fun parseInput(lines: List<String>): List<Pair<Range,Range>> {
        return lines.map { it.split(",").toPair() }
                .map {
                    it.mapBoth { b ->
                        b.split("-")
                            .toPair()
                            .mapBoth { s -> s.toLong() }
                    }
                }
    }

}

fun Range.toLongRange() = first..second

fun Range.overlap(other: Range) =
    toLongRange().contains(other.first) ||
            toLongRange().contains(other.second)

fun Range.contains(other: Range) =
    first <= other.first && second >= other.second

fun main() {
    val day04 = Day04()
    day04.testPart1(2L, solution = 518)
    day04.testPart2(4L)
}
