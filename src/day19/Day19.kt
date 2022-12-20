package day19

import common.threading.threaded
import common.Challenge
import common.Part
import common.tests
import max
import min
import stripInt
import kotlin.math.ceil

class Day19 : Challenge<List<Blueprint>, Int>() {
    override val day: String = "day19"

    val ore = "ore"
    val clay = "clay"
    val obsidian = "obsidian"
    val geode = "geode"

    val priorities = listOf(geode, obsidian, clay, ore)
    fun String.priority() = priorities.indexOf(this)

    fun Map<String, Int>.addRobot(newType: String): Map<String, Int> =
        toMutableMap().apply {
            compute(newType){ _, v -> (v?:0) + 1 }
        }

    fun Map<String, Int>.collectResources(current: Map<String, Int>) =
        entries.associate { (type, num) ->
            type to num + (current[type] ?: 0)
        }

    fun Blueprint.solve(robots: Map<String, Int>, resources: Map<String, Int>, timeLeft: Int): Int {
        if (timeLeft == 0) {
            return (resources[geode] ?: 0).also {
                if(it > best) best = it
            }
        }

        // collect resources with robots
        val newResources = robots.collectResources(resources)

        // if there's no possible way to beat the current best then quit
        //  e.g. if we could build a geode bot every turn
        val currentGeodeProjection = (newResources[geode] ?: 0) + (robots[geode]?:0)*timeLeft
        val potentialMax = (timeLeft+1) * timeLeft/2.0 + currentGeodeProjection
        if(potentialMax < best) {
            return 0
        }

        // determine what factory should build
        var builds = getPossibleBuilds(resources)

        // determine if we'll be able to build something new if we wait
        val futureResources = robots.mapValues { it.value*100 }
        val futureBuilds = getPossibleBuilds(futureResources)
            .filterNot { builds.contains(it) }

        // for each robot we can build in the future, but not now, skip ahead until when we can build it
        val waitForBuild = futureBuilds.maxOfOrNull {
            // if we can build it next turn, it won't actually be built until the following turn, hence + 1
            val turnsToBuild = this.robots[it]!!.maxOf { (resource, quantityNeeded) ->
                ceil((quantityNeeded - (resources[resource]?:0)).toDouble() / robots[resource]!!)
            }.toInt() + 1
            val calcResources = build(it, robots.entries.associate { (type, num) ->
                type to (num*turnsToBuild) + (resources[type] ?: 0)
            })
            if(turnsToBuild <= timeLeft)
                solve(robots.addRobot(it), calcResources, timeLeft - turnsToBuild)
            else 0
        } ?: 0

        // don't build things where we're already producing the max needed
        builds = builds.filter {
            val max = maxNeeded[it] ?: 1000
            it == geode || max > (robots[it]?:0)
        }

        // always build geodes if possible
        if (builds.contains(geode)) {
            return solve(robots.addRobot(geode), build(geode, newResources), timeLeft - 1)
        }

        // calculate for each possible build
        val bestBuild = builds.maxOfOrNull { newType ->
            solve(
                robots.addRobot(newType),
                build(newType, newResources),
                timeLeft - 1
            )
        } ?: 0

        return max(waitForBuild, bestBuild)
    }

    override fun part1(input: List<Blueprint>): Int {
        val startingRobot = mapOf(ore to 1)
        return threaded(input.size) {
            val blueprint = input[it]
            blueprint.reset()
            val geodes = blueprint.solve(startingRobot, mapOf(), 24)
            // println("Blueprint ${blueprint.id} - $geodes")
            blueprint.id*geodes
        }.sum()
    }

    override fun part2(input: List<Blueprint>): Int {
        val startingRobot = mapOf(ore to 1)
        return threaded(min(3, input.size)) {
            val blueprint = input[it]
            blueprint.reset()
            val geodes = blueprint.solve(startingRobot, mapOf(), 32)
            // println("Blueprint ${blueprint.id} - $geodes")
            geodes
        }.let { it[0] * it[1] * (it.getOrNull(2)?:1) }
    }

    private val costRegex = Regex("(\\d+) ($ore|$clay|$obsidian|$geode)")
    private fun String.getCosts() =
        costRegex.findAll(this).map {
            it.groupValues[2] to it.groupValues[1].toInt()
        }.toList()
    override fun parseInput(lines: List<String>): List<Blueprint> {
        return lines.map { line ->
            line.split(Regex("[.:]")).let {
                Blueprint(it[0].stripInt(), mapOf(
                    geode to it[4].getCosts(),
                    obsidian to it[3].getCosts(),
                    clay to it[2].getCosts(),
                    ore to it[1].getCosts(),
                ))
            }
        }
    }

    override val tests = tests<Int> {
        file("test", part1 = 33, part2 = 56*62)
        file("input", part1 = 978, part2 = 15939)
    }

}

fun main() {
    Day19().test(Part.BOTH)
}
