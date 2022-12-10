package day10

import common.Challenge
import common.Part
import common.tests
import groupByLines
import mapCumulatively
import print
import kotlin.math.abs

class Day10 : Challenge<List<String>, Int>() {
    override val day: String = "day10"

    fun List<String>.parseCommands(): List<Int> =
        listOf(0) + flatMap {
            if(it.startsWith("addx"))
                listOf(0, it.substringAfter(" ").toInt())
            else listOf(0)
        }

    override fun part1(input: List<String>): Int {
        return input.parseCommands()
            .mapCumulatively(1){ last, item ->
                last + item
            }
            .let {
                it.strength(20) +
                        it.strength(60) +
                        it.strength(100) +
                        it.strength(140) +
                        it.strength(180) +
                        it.strength(220)
            }
    }

    fun List<Int>.strength(cycle: Int) =
        get(cycle-1)*cycle

    override fun part2(input: List<String>): Int {
        input.parseCommands()
            .mapCumulatively(1){ last, item ->
                last + item
            }.mapIndexed { idx, x ->
                val cycle = idx-2
                val y = (idx) % 40
                if(abs(x-y)<=1) "#" else "."
            }.groupByLines(40)
            .map { it.joinToString("") }
            .print()

        return 0
    }

    override fun parseInput(lines: List<String>): List<String> {
        return lines
    }

    // part2 = "EGLHBLFJ"
    override val tests = tests<Int> {
        file("test", part1 = 13140)
        file("input", part1 = 13920)
    }

}

fun main() {
    Day10().test(Part.BOTH)
}
