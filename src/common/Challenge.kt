package common

import java.nio.file.Paths
import kotlin.io.path.readLines

abstract class Challenge<I,R> {

    abstract val day: String

    abstract fun part1(input: I): R

    abstract fun part2(input: I): R

    abstract fun parseInput(lines: List<String>): I

    private fun getInput(file: String): I =
        parseInput(Paths.get("src/$day/$file.txt").readLines())

    fun testPart1(expected: R){
        val testInput = getInput("test")
        checkAnswer(part1(testInput), expected, "testInput part1")

        val input = getInput("input")
        println(part1(input))
    }

    fun testPart2(expected: R){
        val testInput = getInput("test")
        checkAnswer(part2(testInput), expected, "testInput part2")

        val input = getInput("input")
        println(part2(input))
    }

    private fun checkAnswer(actual: R, expected: R, name: String = "Testing") {
        check(actual == expected) { "$name: Actual val: $actual" }
        println("$name: Success")
    }
}