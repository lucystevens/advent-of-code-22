package day17

import common.Challenge
import common.Part
import common.Point
import common.mapping.Grid
import common.tests
import computeIf
import groupUntil
import min
import toGrid
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.readLines
import kotlin.random.Random

class Day17 : Challenge<List<Char>, Long>() {
    override val day: String = "day17"

    val rocks = Paths.get("src/$day/rocks.txt")
        .readLines()
        .groupUntil { it.isBlank() }
        .map { rock ->
            val height = rock.size
            rock.map { it.toCharArray().toList() }
                .toGrid().map.entries
                .filter{ (_,v) -> v == '#' }
                .map { it.key.transform(0, (height-1)*-1) }
        }


    fun generateRock(left: Int, bottom: Int, index: Long) =
        rocks[(index%5).toInt()].map {
            it.transform(left, bottom)
        }

    fun Set<Point>.containsLine(y: Int, width: Int) =
        containsAll((1..width)
            .map { Point(it, y) })

    fun List<Char>.simulate(iterations: Long, startJetsAt: Int = 0, startCountAt: Long = 0): Long{
        val width = 7
        val points = (1..width)
            .map { Point(it, 0) }
            .toMutableSet()

        val leftWall = 0
        val rightWall = leftWall + width + 1
        var top = 0 // y goes negative the higher we get, don't ask
        var count = startCountAt
        val jetIndex = AtomicInteger(startJetsAt)
        while(count < iterations){
            val rock = generateRock(leftWall + 3, top - 4, count)
            val restingPoints = fall(this, jetIndex, rock, points, leftWall, rightWall)
            val newTop = restingPoints.minOfOrNull { it.y }!!
            top = min(top, newTop)
            points.addAll(restingPoints)
            count++

            val hasFlatTop = points.containsLine(top, width)
            /*val highestFullLine = restingPoints.map { it.y }
                .toSet()
                .filter { points.containsLine(it, width) }
                .minOrNull()*/
                if(hasFlatTop){
                    println("Flat top at $top (after rock $count) jet index = ${jetIndex.get()}")
                    // first 926 rocks adds 1492
                    // every following 1695 rocks adds 2671 height
                    // final 1574 rocks add 2495
                }
                //points.removeIf { it.y > highestFullLine }
        }
        return top*-1L
    }

    fun fall(jets: List<Char>, jetIndex: AtomicInteger, falling: List<Point>, fixed: Set<Point>, leftWall: Int, rightWall: Int): List<Point>{
        var rocks = falling
        var invalid = false
        while(!invalid){
            jetIndex.set(jetIndex.get() % jets.size)
            val jet = jets[jetIndex.getAndIncrement()]
            var nextRocks = rocks.map { jet.moveRock(it) }
            invalid = nextRocks.any {
                it.x <= leftWall || it.x >= rightWall || fixed.contains(it)
            }
            if(!invalid) rocks = nextRocks

            // fall with gravity
            nextRocks = rocks.map { it.below() }
            invalid = nextRocks.any {
                fixed.contains(it)
            }
            if(!invalid) rocks = nextRocks
        }
        return rocks
    }

    fun Char.moveRock(rock: Point) =
        when(this){
            '<' -> rock.left()
            '>' -> rock.right()
            else -> error("Bad jet $this")
        }

    fun <T> List<T>.getOrWrap(index: Int): T =
        get(index % size)

    fun <T> List<T>.forEachInfinitely(fn: (T) -> Boolean){
        var run = true
        var idx = 0
        while(run){
            run = fn(get(idx))
            idx = (idx+1) % size
        }
    }

    override fun part1(input: List<Char>): Long {
        return input.simulate(2022)
    }

    fun List<Char>.calculate(iterations: Long): Long{
        var top = 1492L
        val rocks = iterations-926
        val loops = rocks/1695
        val remaining = rocks%1695
        top += loops*2671
        /*while(rocks > 1695){
            rocks -= 1695
            top += 2671
            println("top: $top after rock: ${iterations-rocks}")
        }*/
        val simulated = simulate(remaining, 5307, 1)
        println("Top: $top for remaining $remaining")
        println("Simulated: $simulated")
        return simulated + top
    }

    // 2884449245613 too high
    // 1575811209487 correct
    override fun part2(input: List<Char>): Long {
        val test = Random.nextLong(10000, 100000)
        println("Testing: $test")
        val sim = input.simulate(test)
        val calc = input.calculate(test)
        println("Sim: $sim, Calc: $calc")
        //return input.calculate(1000000000000)
        return 0
    }

    override fun parseInput(lines: List<String>): List<Char> {
        return lines[0].toCharArray().toList()
    }

    // ended up being off by 1, still not 100% sure where the issue was
    override val tests = tests<Long> {
        //file("test", part1 = 3068, part2 = 1514285714288)
        file("input", part1 = 3215, part2 =  1575811209487)
    }

}

fun main() {
    Day17().test(Part.BOTH)
}
