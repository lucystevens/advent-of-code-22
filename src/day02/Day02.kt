package day02

import common.Challenge
import toPair
import kotlin.math.abs

class Day02 : Challenge<List<Pair<Int, Int>>, Long>() {
    override val day: String = "day02"

    override fun part1(input: List<Pair<Int, Int>>): Long {
        return input.sumOf {
            (abs(it.first - it.second+1) % 3L)*3 + (it.second - 87L)
        }
    }

    override fun part2(input: List<Pair<Int, Int>>): Long {
        return input.sumOf {
            (abs(it.first + it.second+2) % 3L) + 1 + (it.second - 88L)*3
        }
    }

    override fun parseInput(lines: List<String>): List<Pair<Int, Int>> {
        return lines.map { line ->
            line.split(" ")
                .map { it.trim()[0].code }.toPair()
        }
    }

}

fun main() {
    val day02 = Day02()
    day02.testPart1(15L, solution = 10310L)
    day02.testPart2(12L, solution = 14859L)
}
