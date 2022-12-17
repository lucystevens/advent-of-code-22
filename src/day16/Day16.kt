package day16

import common.Challenge
import common.Part
import common.tests
import max
import stripLong

class Day16 : Challenge<Map<String,Valve>, Long>() {
    override val day: String = "day16"

    var valvesToOpen = 0
    fun Map<String,ValveNode>.solve(me: Actor, open: Set<String>, timeLeft: Int): Long {
        // if out of time, no more pressure released
        if(timeLeft == 0){
            return 0L
        }

        val newOpen = open.toMutableSet()
        if(me.timeLeft == 1) newOpen += me.current.key

        // calc pressure release for this minute
        val pressure = newOpen.sumOf { get(it)!!.flow }

        val myMoves =
            if(me.timeLeft > 1) listOf(me.decrementTime())
            else me.getMoves(newOpen, this)

        // if all valves (with some flow) are open, calculate all remaining pressure
        if(newOpen.size == valvesToOpen){
            return timeLeft * pressure
        }

        // get possible next step with highest pressure release
        return pressure + (myMoves.maxOfOrNull {
            solve(it, newOpen, timeLeft-1)
        } ?: 0)
    }

    var totalPressure = 0L
    fun Map<String,ValveNode>.solveWithElephant(me: Actor, elephant: Actor, open: Set<String>, timeLeft: Int, previousBest: Long): Long {
        // if out of time, no more pressure released
        if(timeLeft == 0){
            return 0L
        }

        val newOpen = open.toMutableSet()
        if(me.timeLeft == 1) newOpen += me.current.key
        if(elephant.timeLeft == 1) newOpen += elephant.current.key

        // calc pressure release for this minute
        val pressure = newOpen.sumOf { get(it)!!.flow }

        // assuming all remaining valves opened instantly, would this be a better pressure?
        if(totalPressure*timeLeft <= previousBest){
            return 0L
        }

        val myMoves =
            if(me.timeLeft > 1) listOf(me.decrementTime())
            else me.getMoves(newOpen, this)

        val elephantMoves =
            if(elephant.timeLeft > 1) listOf(elephant.decrementTime())
            else elephant.getMoves(newOpen, this)

        // if all valves (with some flow) are open, calculate all remaining pressure
        if(newOpen.size == valvesToOpen){
            return timeLeft * pressure
        }

        // get possible next step with highest pressure release
        var max = 0L
        myMoves.flatMap { m ->
            elephantMoves.map { e -> m to e}
        }.filterNot { (m,e) ->
            newOpen.size != valvesToOpen-1 && m.current.key == e.current.key } // don't move to same place unless it's the only move
            .forEach { (m,e) ->
                val res = solveWithElephant(m, e, newOpen, timeLeft-1, max)
                max = max(max, res)
            }

        return max + pressure
    }

    fun Map<String,ValveNode>.solveWithElephant(): Long {
        val start = this["AA"]!!
        val actor = Actor(start, 0)

        val myMoves = actor.getMoves(setOf(), this)

        // get possible next step with highest pressure release
        val pairs = mutableSetOf<Long>()
        var max = 0L
        myMoves.flatMap { m ->
            myMoves.map { e -> m to e}
        }.filterNot { (m,e) ->
            m.current.key == e.current.key ||
                    pairs.containsAndAdd(m.current.key.hashCode().toLong() * e.current.key.hashCode())
        } // don't move to same place and don't compute the same pair
        .forEach { (m,e) ->
            val res = solveWithElephant(m, e, setOf(), 25, max)
            max = max(max, res)
        }

        return max
    }

    fun <T> MutableSet<T>.containsAndAdd(element: T): Boolean {
        val contains = contains(element)
        add(element)
        return contains
    }


    private fun Map<String,Valve>.toNodes(): Map<String, ValveNode> =
        mapValues { (k,v) ->
                ValveNode(
                    k,
                    v.flow,
                    findDistances(v)
                )
            }

    private fun Map<String,Valve>.findDistances(valve: Valve): Map<String, Int> =
        values.filterNot { it.key == valve.key || it.flow == 0L }
            .map { it to findDistance(valve.key, it.key) }
            .sortedByDescending { (v,d) -> v.flow - d }
            .associate { it.first.key to it.second }

    // BFS
    private fun Map<String,Valve>.findDistance(valve: String, dest: String): Int{
        var distance = 0
        var points = setOf(valve)
        while (!points.contains(dest)){
            points = points.flatMap { get(it)!!.tunnels }.toSet()
            distance++
        }
        return distance
    }

    override fun part1(input: Map<String,Valve>): Long {
        val nodes = input.toNodes()
        val start = nodes["AA"]!!
        valvesToOpen = input.values.count { it.flow > 0 }
        val actor = Actor(start, 0)
        return nodes.solve(actor, setOf(), 30)
    }

    override fun part2(input: Map<String,Valve>): Long {
        totalPressure = input.values.sumOf { it.flow }
        val nodes = input.toNodes()
        valvesToOpen = input.values.count { it.flow > 0 }
        return nodes.solveWithElephant()
    }

    val tunnelRegex = Regex("[A-Z]{2}")
    override fun parseInput(lines: List<String>): Map<String,Valve> {
        return lines.map {
            val flow = it.stripLong()
            tunnelRegex.findAll(it).let { tunnels ->
                Valve(
                    tunnels.first().value,
                    flow,
                    tunnels.drop(1).map { t -> t.value }.toList()
                )
            }
        }.associateBy { it.key }
    }

    // real input for part2 takes ~20 minutes!
    override val tests = tests<Long> {
        file("test", part1 = 1651, part2 = 1707)
        file("input", part1 = 1595, part2 = 2189)
    }

}

data class Valve(
    val key: String,
    val flow: Long,
    val tunnels: List<String>
)

data class ValveNode(
    val key: String,
    val flow: Long,
    val tunnels: Map<String, Int>
)

fun main() {
    Day16().test(Part.BOTH)
}
