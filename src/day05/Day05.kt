package day05

import common.Challenge
import common.tests
import getOrAdd
import multiPop
import toStack
import java.util.*

class Day05 : Challenge<Pair<List<Stack<String>>, List<BoxMove>>, String>() {
    override val day: String = "day05"

    override fun part1(input: Pair<List<Stack<String>>, List<BoxMove>>): String {
        val stacks = input.first
        input.second.forEach { move ->
            repeat(move.quantity){
                stacks[move.to].push(stacks[move.from].pop())
            }
        }
        return stacks.drop(1).joinToString("") { it.pop() }
    }

    override fun part2(input: Pair<List<Stack<String>>, List<BoxMove>>): String {
        val stacks = input.first
        input.second.forEach { move ->
            stacks[move.from].multiPop(move.quantity).forEach {
                stacks[move.to].push(it)
            }
        }
        return stacks.drop(1).joinToString("") { it.pop() }
    }

    override fun parseInput(lines: List<String>): Pair<List<Stack<String>>, List<BoxMove>> {
        val stacks = mutableListOf<MutableList<String>>()
        val moves = mutableListOf<BoxMove>()
        var writeStack = true
        lines.forEach { line ->
            if(writeStack) {
                Regex("[A-Z]").findAll(line).forEach { match ->
                    val stackIdx = (match.range.first + 3) / 4
                    stacks.getOrAdd(stackIdx) { mutableListOf() }
                        .add(match.value)
                }
                if(line.isBlank()){
                    writeStack = false
                }
            }
            else {
                val matches = Regex("\\d+").findAll(line)
                    .map { it.value.toInt() }
                    .toList()
                moves.add(BoxMove(matches[0], matches[1], matches[2]))
            }
        }
        return stacks.map { it.reversed().toStack() } to moves
    }

    override val tests = tests<String> {
        file("test", part1 = "CMZ", part2 = "MCD")
        file("input", part1 = "VQZNJMWTR", part2 = "NLCDCLVMQ")
    }

}

data class BoxMove (
    val quantity: Int,
    val from: Int,
    val to: Int)

fun main() {
    Day05().test()
}
