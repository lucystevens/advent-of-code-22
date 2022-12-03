package day03

import InputType
import ReturnType
import common.Challenge

class Day03 : Challenge<InputType, ReturnType>() {
    override val day: String = "day03"

    override fun part1(input: InputType): ReturnType {
        return input.size.toLong()
    }

    override fun part2(input: InputType): ReturnType {
        return input.size.toLong()
    }

    override fun parseInput(lines: List<String>): InputType {
        return lines
    }

}

fun main() {
    val day03 = Day03()
    day03.testPart1(1L)
    // day03.testPart2(1L)
}
