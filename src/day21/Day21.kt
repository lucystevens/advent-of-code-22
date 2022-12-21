package day21

import ReturnType
import common.Challenge
import common.Part
import common.expression.Expression
import common.expression.Operator
import common.tests
import kotlin.math.exp

class Day21 : Challenge<Map<String,String>, ReturnType>() {
    override val day: String = "day21"

    val numberRegex = Regex("-?\\d+")
    val expressionRegex = Regex("[a-z]{4}")
    val operators = setOf("*", "/", "+", "-")
    fun Map<String,String>.buildExpression(): String{
        var expression = get("root")!!
        while (expression.contains(expressionRegex)) {
            expression = expressionRegex.replace(expression){
                val sub = get(it.value) ?: error("Missing ${it.value}")
                "($sub)"
            }
        }
        // expected
        return expression
    }

    override fun part1(input: Map<String,String>): ReturnType {
        val expression = input.buildExpression()
        return Expression(expression).evaluate()
    }

    override fun part2(input: Map<String,String>): ReturnType {
        val newInput = input.toMutableMap().apply {
            set("humn", "me")
            compute("root"){ _,v -> v!!.replace("+", "=") }
        }
        val expression = newInput.buildExpression()
        val sides = expression.split("=").map {
            if(it.contains("me")) it
            else Expression(it).evaluate().toString()
        }.sortedBy { it.length }
        val toSolve = Expression(Expression(sides[1]).simplify())
        return toSolve.solveFor("me", sides[0].toLong())
    }

    override fun parseInput(lines: List<String>): Map<String,String> {
        return lines.associate { line ->
            line.split(": ").let { it[0] to it[1] }
        }
    }

    override val tests = tests<ReturnType> {
        file("test", part1 = 152, part2 = 301)
        file("input", part1 = 291425799367130, part2 = 3219579395609)
    }

}


fun main() {
    Day21().test(Part.BOTH)
}
