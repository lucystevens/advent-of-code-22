package day03

import common.Challenge

class Day03 : Challenge<List<String>, Long>() {
    override val day: String = "day03"

    override fun part1(input: List<String>): Long {
        return input.map { it.halve() }
                .map { intersect(it.first, it.second) }
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

}

fun <T> List<T>.print(): List<T> = map {
    it.also { println(it) }
}

fun Char.toPriority(): Int =
    if(code > 90) code - 96
    else code - 38

fun String.halve(): Pair<String, String> =
    substring(0, length/2) to substring(length/2)

fun intersect(string1: String, string2: String): Set<Char> =
    string1.toCharArray()
        .intersect(string2
            .toCharArray()
            .asIterable()
            .toSet())

fun List<String>.findCommonChars(): String =
    fold(first()){ last, string ->
        intersect(last, string).joinToString()
    }

fun <T> List<T>.groupByLines(linesPerGroup: Int): List<List<T>> =
    foldIndexed(mutableMapOf<Int, MutableList<T>>()){ idx, res, line ->
        val group = idx / linesPerGroup
        res.computeIfAbsent(group){ mutableListOf() }
            .add(line)
        res
    }.values.toList()

fun main() {
    val day03 = Day03()
    day03.testPart1(157, solution = 7795)
    day03.testPart2(70)
}
