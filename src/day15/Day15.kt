package day15

import ReturnType
import common.Challenge
import common.Part
import common.Point
import common.tests
import mapBoth
import max
import stripInts
import toPair
import java.util.concurrent.atomic.AtomicInteger

class Day15 : Challenge<List<Sensor>, ReturnType>() {
    override val day: String = "day15"

    val yRow = AtomicInteger(10)
    override fun part1(input: List<Sensor>): ReturnType {
        val minX = input.minOf { it.sensor.x - it.distance }
        val maxX = input.maxOf { it.sensor.x + it.distance }
        val y = this.yRow.getAndSet(2000000)
        return (minX..maxX).count { x ->
            val point = Point(x,y)
            input.firstOrNull { point != it.beacon && it.contains(point) } != null
        }.toLong()
    }

    val axisMax = AtomicInteger(20)
    override fun part2(input: List<Sensor>): ReturnType {
        val max = axisMax.getAndSet(4000000)
        var x = 0
        (0..max).forEach { y ->
            while (x <= max){
                var xMod = 1
                val beacon = input.all { sensor ->
                    // check distance and if in range of sensor
                    val distanceFromSensor = sensor.sensor.distanceTo(Point(x,y)).manhattan()
                    val isInRange = distanceFromSensor <= sensor.distance

                    // if in range, then skip ahead the maximum distance to be just outside the sensor
                    if(isInRange){
                        xMod = max(xMod, (sensor.distance-distanceFromSensor)+1)
                    }
                    !isInRange
                }

                // beacon will be true if in range of no sensors
                if(beacon){
                    return x*4000000L + y
                }
                x += xMod
            }
            x = 0
        }
        error("No beacon found!")
    }

    override fun parseInput(lines: List<String>): List<Sensor> {
        return lines.map { line ->
            line.split(":").toPair().mapBoth { 
                it.stripInts().toPair().let { p -> Point(p.first,p.second) }
            }.let { Sensor(it.first, it.second) }
        }
    }

    override val tests = tests<ReturnType> {
        file("test", part1 = 26, part2 = 56000011)
        file("input", part1 = 5335787, part2 = 13673971349056)
    }

}

// ~40486114136232 points for input
val pointsForDistances = mutableMapOf(1L to 5L)
fun Long.numberOfPoints(): Long = pointsForDistances.computeIfAbsent(this) {
    it*2 + 1 * it*it*2
}

data class Sensor(
    val sensor: Point,
    val beacon: Point
){
    val distance = sensor.distanceTo(beacon).manhattan()
    fun contains(point: Point) =
        sensor.distanceTo(point).manhattan() <= distance
}

fun main() {
    Day15().test(Part.BOTH)
}
