package day03

import common.Challenge
import common.tests
import groupByLines
import intersect

class Day03 : Challenge<List<String>, Long>() {
    override val day: String = "day03"

    override fun part1(input: List<String>): Long {
        return input.map { it.halve() }
                .map { it.first.intersect(it.second) }
                .sumOf { it.first().toPriority().toLong() }
    }

    override fun part2(input: List<String>): Long {
        return input.groupByLines(3)
            .map { it.findCommonChars() }
            .sumOf { it[0].toPriority().toLong() }
    }

    override fun parseInput(lines: List<String>): List<String> {
        return lines
    }

    override val tests = tests<Long> {
        file("test", part1 = 157, part2 = 70)
        file("input", part1 = 7795, part2 = 2703)
    }

}

fun Char.toPriority(): Int =
    if(code > 90) code - 96
    else code - 38

fun String.halve(): Pair<String, String> =
    substring(0, length/2) to substring(length/2)

fun List<String>.findCommonChars(): String =
    fold(first()){ last, string ->
        last.intersect(string).joinToString()
    }

fun main() {
    Day03().test()
}
