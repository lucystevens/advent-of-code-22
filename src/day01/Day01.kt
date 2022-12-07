package day01

import common.Challenge
import common.tests

class Day01 : Challenge<List<List<String>>, Long>() {
    override val day: String = "day01"

    override fun part1(input: List<List<String>>): Long {
        return input.maxOf { elf ->
            elf.sumOf { it.toLong() }
        }
    }

    override fun part2(input: List<List<String>>): Long {
        return input.map { elf ->
            elf.sumOf { it.toLong() }
        }.sortedDescending().let {
            it[0] + it[1] + it[2]
        }
    }

    override fun parseInput(lines: List<String>): List<List<String>> {
        val input = mutableListOf<List<String>>()
        var currentElf = mutableListOf<String>()
        lines.forEach {
            if (it.isBlank()){
                input += currentElf
                currentElf = mutableListOf()
            }
            else {
                currentElf += it
            }
        }
        input += currentElf
        return input
    }

    override val tests = tests<Long> {
        file("test", part1 = 24000L, part2 = 45000L)
        file("input", part1 = 65912L, part2 = 195625L)
    }

}

fun main() {
    Day01().test()
}
