package day04

import common.Challenge
import common.tests
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

    override val tests = tests<Long> {
        file("test", part1 = 2, part2 = 4)
        file("input", part1 = 518, part2 = 909)
    }

}

fun Range.toLongRange() = first..second

fun Range.overlap(other: Range) =
    toLongRange().contains(other.first) ||
            toLongRange().contains(other.second)

fun Range.contains(other: Range) =
    first <= other.first && second >= other.second

fun main() {
    Day04().test()
}
