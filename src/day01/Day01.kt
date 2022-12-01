package day01

import common.Challenge

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

}

fun main() {
    val day01 = Day01()
    day01.testPart1(24000L)
    day01.testPart2(45000L)
}
