package day11

import common.Challenge
import common.Part
import common.tests
import groupUntil
import stripInt
import stripLongs
import toPair
import kotlin.random.Random

class Day11 : Challenge<List<Monkey>, Long>() {
    override val day: String = "day11"

    fun List<Monkey>.simulate(rounds: Int, modifyWorry: (Long) -> Long): Long {
        repeat(rounds){
            forEach { monkey ->
                while(monkey.items.isNotEmpty()){
                    val res = monkey.inspectAndThrow(modifyWorry)
                    get(res.second).items += res.first
                }
            }
        }
        return map { it.inspectCount }
            .sortedDescending()
            .let { it[0]*it[1] }
    }

    override fun part1(input: List<Monkey>): Long {
        return input.simulate(20){ it / 3}
    }

    override fun part2(input: List<Monkey>): Long {
        val monkeyFactor = input.map { it.testDivisibleBy }
            .fold(1L){ total, it -> total * it}
        return input.simulate(10000) { it % monkeyFactor }
    }

    override fun parseInput(lines: List<String>): List<Monkey> {
        return lines.groupUntil { it.isBlank() }.map {
            Monkey(
                it[0].stripInt(),
                it[1].stripLongs().toMutableList(),
                it[2].substringAfter("new ="), // TODO parse operation
                it[3].stripInt(),
                it[4].stripInt(),
                it[5].stripInt()
            )
        }.sortedBy { it.id }
    }

    // 101, 95, 7, 105
    // 304, 32, 36, 319, 301, 65, 27, 321
    override val tests = tests<Long> {
        file("test", part1 = 10605, part2 = 2713310158L)
        file("input", part1 = 102399, part2 = 23641658401)
    }

}

data class Monkey (
    val id: Int,
    val items: MutableList<Long>,
    val worryExpression: String,
    val testDivisibleBy: Int,
    val ifTrueThrowTo: Int,
    val ifFalseThrowTo: Int
) {
    var inspectCount = 0L

    fun inspectAndThrow(modifyWorry: (Long) -> Long): Pair<Long,Int>{
        inspectCount++
        var worry = items.removeAt(0)
        worry = modifyWorry(worryExpression.executeExpression(worry))
        return worry to when(worry % testDivisibleBy.toLong()) {
            0L -> ifTrueThrowTo
            else -> ifFalseThrowTo
        }
    }

}

// TODO make this reuseable for future
val operators = listOf('*', '-', '+', '/')
fun String.executeExpression(oldVar: Long): Long{
    val subbed = replace("old", "$oldVar")
    val nums = subbed.stripLongs().toPair()
    return when(val operator = find { it in operators }){
        '*' -> nums.first * nums.second
        '-' -> nums.first - nums.second
        '+' -> nums.first + nums.second
        '/' -> nums.first / nums.second
        else -> error("Bad operator $operator")
    }
}

fun main() {
    Day11().test(Part.BOTH)
    //testSimplifiedDivision()
}

// test that using mod of a common factor can reduce large number calculations
fun testSimplifiedDivision(){
    val nums = listOf(23L, 19L, 13L, 17L)
    val commonFactor = nums.fold(1L){ total, it -> total * it}
    repeat(10){
        val toTest = Random.nextLong(Long.MAX_VALUE/10)
        val shortened = toTest % commonFactor
        nums.forEach { num ->
            println("$toTest % $num - Actual: ${toTest%num}, Shortened: ${shortened%num}")
            println("$toTest*5 % $num - Actual: ${(toTest*5)%num}, Shortened: ${(shortened*5)%num}")
            println("$toTest+5 % $num - Actual: ${(toTest+5)%num}, Shortened: ${(shortened+5)%num}")
        }
    }
}
