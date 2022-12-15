package common

import checkAnswer

abstract class Challenge<I,R> : Testable {

    abstract val day: String

    abstract fun part1(input: I): R

    abstract fun part2(input: I): R

    abstract fun parseInput(lines: List<String>): I

    abstract val tests: List<Test<R>>

    override fun test(toRun: Part){
        println("Running tests for $day")
        if(toRun.runPart1){
            tests.forEach {
                val input = parseInput(it.getInput(day))
                testInternal(input, it.part1, it.name, "part1", ::part1)
            }
        }
        println()
        if(toRun.runPart2){
            tests.forEach {
                val input = parseInput(it.getInput(day))
                testInternal(input, it.part2, it.name, "part2", ::part2)
            }
        }
    }

    private fun <R> timed(fn: () -> R): Pair<Long, R> {
        val start = System.currentTimeMillis()
        val res = fn()
        return System.currentTimeMillis() - start to res
    }

    private fun testInternal(input: I, expected: R?, name: String, part: String, fn: (I) -> R){
        val (time, result) =  timed { fn(input) }
        if(expected == null) println("$name $part: $result (in ${time}ms)")
        else checkAnswer(result, expected, "$name $part", time)
    }

}