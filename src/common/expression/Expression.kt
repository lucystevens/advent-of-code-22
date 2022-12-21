package common.expression

import kotlin.math.exp

// evaluate mathematical expressions
// since this is advent of code, it's assumed all expressions are valid
class Expression(private val expression: String) {

    fun evaluate(vararg variables: Pair<String, Long>): Long {
        var exp = expression
        val variableRegex = Regex("[a-zA-Z]+")
        var varMatch = variableRegex.find(exp)
        while (varMatch != null){
            val value = variables.find { it.first == varMatch!!.value }
                ?: error("Missing variable ${varMatch.value}")
            exp = exp.replaceRange(varMatch.range, "${value.second}")
            varMatch = variableRegex.find(exp)
        }

        val regex = Regex("\\(([^()]+)\\)")
        var match = regex.find(exp)
        while (match != null){
            val result = evaluateWithinBrackets(match.groupValues[1])
            exp = exp.replaceRange(match.range, "$result")
            match = regex.find(exp)
        }
        return evaluateWithinBrackets(exp)
    }

    // evaluate an expression without brackets
    private fun evaluateWithinBrackets(expression: String): Long{
        var exp = expression
        operators.forEach { op ->
            var match = op.expressionRegex.find(exp)
            while (match != null){
                val result = evaluateSimple(
                    op.symbol,
                    match.groupValues[1].toLong(),
                    match.groupValues[2].toLong(),
                )
                exp = exp.replaceRange(match.range, "$result")
                match = op.expressionRegex.find(exp)
            }
        }
        return exp.trim().toLong()
    }

    // evaluates expressions with a single operation
    private fun evaluateSimple(operator: Char, num1: Long, num2: Long) =
            getOperator(operator).operation(num1, num2)

    fun simplify(): String {
        var exp = expression
        val regex = Regex("\\(([^()a-zA-Z]+)\\)")
        var match = regex.find(exp)
        while (match != null){
            val result = evaluateWithinBrackets(match.groupValues[1])
            exp = exp.replaceRange(match.range, "$result")
            match = regex.find(exp)
        }
        return exp
    }

    fun solveFor(variable: String, whereEquals: Long): Long{
        var exp = expression
        var result = whereEquals
        while(exp != variable) {
            val (left, op, right) = splitIntoParts(exp)
            val (withVar, noVar) =
                if (left.contains(variable)) left to right
                else right to left
            val value = Expression(noVar).evaluate()
            result = when {
                op.symbol in listOf('*', '+') -> op.opposite(result, value)
                op.symbol == '-' && left.contains(variable) -> result + value
                op.symbol == '-' && right.contains(variable) -> value - result
                op.symbol == '/' && left.contains(variable) -> result * value
                op.symbol == '/' && right.contains(variable) -> value / result
                else -> error("Missing case for operator ${op.symbol}")
            }
            exp = withVar.trim().removeSurrounding("(", ")")
        }
        return result
    }

    private fun splitIntoParts(expression: String): Triple<String, Operator, String> {
        val exp = expression.trim().removeSurrounding("(", ")")
        var level = 0
        exp.forEachIndexed{ idx, c ->
            if(c == '(') level++
            else if( c == ')') level--

            if(level == 0 && c.isOperator()){
                return Triple(
                    exp.substring(0, idx).trim(),
                    getOperator(c),
                    exp.substring(idx+1).trim()
                )
            }
        }
        error("Could not split expression $exp")
    }

}