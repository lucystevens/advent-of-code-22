package day09

import common.Challenge
import common.Part
import common.Point
import common.tests
import toPair
import update
import kotlin.math.abs

class Day09 : Challenge<List<Pair<String,Int>>, Int>() {
    override val day: String = "day09"

    private fun simulateRope(input: List<Pair<String,Int>>, size: Int): Int {
        val rope = MutableList(size){ Point(0, 0) }
        return input.flatMap { move ->

            // repeat moves
            (1..move.second).map {
                // move head
                rope.update(0) { it.move(move.first) }

                // move rest of rope
                (1 until rope.size).forEach { idx ->
                    rope.update(idx) { knot ->
                        knot.follow(rope[idx - 1])
                    }
                }

                // return last point
                rope.last()
            }

        }.toSet().size
    }

    fun Point.follow(head: Point): Point {
        val distance = distanceTo(head)
        // 2 steps away, same column
        return if(distance.x == 0 && distance.y >= 2){
            if(head.y > y) below() else above()
        } // 2 steps away, same row
        else if(distance.y == 0 && distance.x >= 2){
            if(head.x > x) right() else left()
        } // not touching
        else if (!isAdjacent(head)){
            val tmp = if(head.y > y) below() else above()
            if(head.x > x) tmp.right() else tmp.left()
        }
        else this
    }

    override fun part1(input: List<Pair<String,Int>>): Int {
        return simulateRope(input, 2)
    }

    override fun part2(input: List<Pair<String,Int>>): Int {
        return simulateRope(input, 10)
    }

    override fun parseInput(lines: List<String>): List<Pair<String,Int>> {
        return lines.map { it.split(" ").toPair() }
            .map { it.first to it.second.toInt() }
    }

    override val tests = tests<Int> {
        file("test", part1 = 13, part2 = 1)
        file("test2", part2 = 36)
        file("input", part1 = 6563, part2 = 2653)
    }

}


fun List<Point>.printGrid(filler: String = ".", compute: (Int) -> String = { "X" }){
    (-20..20).forEach { y ->
        (-20..20).forEach { x ->
            val idx = indexOf(Point(x,y))
            print(if(idx < 0) filler else compute(idx))
        }
        println()
    }
}


// 2656 too high
fun main() {
    Day09().test(Part.BOTH)
}
