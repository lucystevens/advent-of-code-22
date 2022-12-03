package template

import InputType
import ReturnType
import common.Challenge

class Template : Challenge<InputType, ReturnType>() {
    override val day: String = "template"

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
    val template = Template()
    template.testPart1(1L)
    // template.testPart2(1L)
}
