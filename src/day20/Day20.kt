package day20

import alsoPrint
import common.Challenge
import common.Part
import common.tests
import print
import java.util.UUID
import kotlin.math.abs

class Day20 : Challenge<List<Unique<Long>>, Long>() {
    override val day: String = "day20"

    fun List<Unique<Long>>.mix(times: Int): List<Unique<Long>> {
        val moved = mutableSetOf<Unique<Long>>()
        val result = toMutableList()
        repeat(times){
            forEach { ele ->
                if(!moved.contains(ele)) {
                    val idx = result.indexOf(ele)
                    val newIdx = result.moveIndex(idx, ele.value)
                    result.move(ele, newIdx)
                    moved.add(ele)
                }
            }
            moved.clear()
        }
        return result
    }

    override fun part1(input: List<Unique<Long>>): Long {
        val result = input.mix(1)
        val baseIndex = result.indexOfFirst { it.value == 0L }
        return result.getWrapped(baseIndex+1000) +
                result.getWrapped(baseIndex+2000) +
                result.getWrapped(baseIndex+3000)
    }

    fun List<Unique<Long>>.getWrapped(index: Int): Long =
        get(index % size).value

    val key = 811589153L
    override fun part2(input: List<Unique<Long>>): Long {
        val result = input.map { Unique(it.value*811589153L) }.mix(10)
        val baseIndex = result.indexOfFirst { it.value == 0L }
        return result.getWrapped(baseIndex+1000) +
                result.getWrapped(baseIndex+2000) +
                result.getWrapped(baseIndex+3000)
    }

    override fun parseInput(lines: List<String>): List<Unique<Long>> {
        return lines.map { Unique(it.toLong()) }
    }

    override val tests = tests<Long> {
        file("test", part1 = 3, part2 = 1623178306)
        file("input", part1 = 8764, part2 = 535648840980)
    }

}

fun <T> List<T>.moveIndex(current: Int, distance: Long): Int {
    val dist = (distance % (size-1)).toInt()
    val direction = if(distance > 0) 1 else -1
    var idx = checkWrap(current, direction)

    repeat(dist * direction) {
        idx = checkWrap(idx+direction, direction)
    }
    return idx
}

fun <T> List<T>.checkWrap(idx: Int, direction: Int): Int =
    if(idx == 0 && direction == -1) size-1
    else if(idx == size-1 && direction == 1) 0
    else idx


fun <T> MutableList<T>.move(item: T, toIdx: Int) {
    remove(item).also { if(!it) println("Could not remove $item") }
    add(toIdx, item)
}

fun main() {
    Day20().test(Part.BOTH)
}

data class Unique<T>(
    val value: T,
    val uuid: String = UUID.randomUUID().toString()
)
