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

    fun testPart1(expected: R, solution: R? = null){
        testFromFile(expected, solution, "part1", ::part1)
    }

    fun testPart2(expected: R, solution: R? = null){
        testFromFile(expected, solution, "part2", ::part2)
    }

    fun runTests(tests: List<Pair<I, R>>, fn: (I) -> R){
        tests.forEachIndexed { idx, it ->
            checkAnswer(fn(it.first), it.second, "extraTest$idx")
        }
    }

    private fun testFromFile(expected: R, solution: R?, name: String, fn: (I) -> R){
        val testInput = getInput("test")
        checkAnswer(fn(testInput), expected, "testInput $name")

        val input = getInput("input")
        val result = fn(input)
        if(solution == null) println(result)
        else checkAnswer(result, solution, "realInput $name")
    }

    private fun checkAnswer(actual: R, expected: R, name: String = "Testing") {
        check(actual == expected) { "$name: Actual val: $actual" }
        println("$name: Success")
    }
}