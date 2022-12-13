package day13

import common.Challenge
import common.Part
import common.tests
import groupUntil
import toPair
import UnaryPair

typealias InputType = List<UnaryPair<String>>

class Day13 : Challenge<InputType, Int>() {
    override val day: String = "day13"

    fun checkPair(pair: Pair<String, String>)
        = pair.first.checkAgainst(pair.second)

    override fun part1(input: InputType): Int {
        return input.map { checkPair(it) }.mapIndexed { index, i ->
            if(i < 0) index+1 else 0
        }.sum()
    }

    val samples = listOf("[[2]]", "[[6]]")
    override fun part2(input: InputType): Int {
        return (input.flatMap { it.toList() } + samples)
            .sortedWith { left, right -> left.checkAgainst(right) }
            .mapIndexedNotNull { idx, it -> if(it in samples) idx+1 else null }
            .let { it[0] * it[1] }
    }

    override fun parseInput(lines: List<String>): InputType {
        return lines.groupUntil { it.isBlank() }.map {
            it.toPair()
        }
    }

    // negative = correct order, 0 = equal, positive = incorrect
    fun String.checkAgainst(other: String): Int {
        return if(this.isInt() && other.isInt()){
            toInt().compareTo(other.toInt())
        }
        else {
            val left = this.toList()
            val right = other.toList()
            left.forEachIndexed { index, leftItem ->
                val rightItem = right.getOrElse(index){ return 1 }
                val result = leftItem.checkAgainst(rightItem)
                if(result != 0) return result
            }
            left.size - right.size
        }
    }

    val intRegex = Regex("-?\\d+")
    fun String.isInt() =
        intRegex.matches(this)

    override val tests = tests<Int> {
        file("test", part1 = 13, part2 = 140)
        file("input", part1 = 6420, part2 = 22000)
    }

}

fun String.toList(): List<String> {
    val list = mutableListOf(StringBuilder())
    var level = 0
    removeSurrounding("[", "]").forEach {
        if(it == ',' && level == 0){
            list.add(StringBuilder())
        }
        else {
            if(it == '[') level++
            else if(it == ']') level--
            list.last().append(it)
        }
    }
    return list.map { it.toString() }.filterNot { it.isEmpty() }
}

fun main() {
    Day13().test(Part.BOTH)
}
