package common.expression

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
}