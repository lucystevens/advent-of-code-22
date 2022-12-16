package day16

import InputType
import ReturnType
import common.Challenge
import common.Part
import common.tests

class Day16 : Challenge<InputType, ReturnType>() {
    override val day: String = "day16"

    override fun part1(input: InputType): ReturnType {
        return 0
    }

    override fun part2(input: InputType): ReturnType {
        return 0
    }

    override fun parseInput(lines: List<String>): InputType {
        return lines
    }

    override val tests = tests<ReturnType> {
        file("test", part1 = 0, part2 = 0)
        file("input")
    }

}

fun main() {
    Day16().test(Part.ONE)
}
