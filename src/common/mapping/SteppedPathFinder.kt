package common.mapping

import common.Point

class SteppedPathFinder<T: Comparable<T>>(
    private val end: Point,
    private val grid: Grid<T>,
    private val canVisit: (GridValue<T>, GridValue<T>) -> Boolean
    ) {

    var includeDiagonals: Boolean = false

    // best routes from a start point
    private val stepsFromEnd = mutableMapOf(end to (end to 0L))

    init { populateMap() }

    private fun populateMap(point: Point = end, steps: Long = 1L){
        point.adjacentPoints(includeDiagonals)
            .filterNot { grid.get(it) == null }
            .filter { canVisit(grid.getValue(it), grid.getValue(point)) }
            .forEach {
                val currentSteps = stepsFromEnd[it]
                if(currentSteps == null || currentSteps.second > steps){
                    stepsFromEnd[it] = point to steps
                    populateMap(it, steps+1)
                }
            }
    }

    fun findShortestPath(from: Point): List<Point> {
        val points = mutableListOf(stepsFromEnd[from]!!.first)
        while(points.last() != end){
            points += stepsFromEnd[points.last()]!!.first
        }
        return points
    }

    fun findShortestDistance(from: Point): Long {
        return stepsFromEnd[from]?.second ?: Long.MAX_VALUE
    }

}