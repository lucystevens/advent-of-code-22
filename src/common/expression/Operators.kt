package common.expression

typealias Operation = (Long, Long) -> Long

val operators = listOf(
    Operator('*', "\\*", 1, Long::times, Long::div),
    Operator('/', "/", 2, Long::div, Long::times),
    Operator('+', "\\+", 3, Long::plus, Long::minus),
    Operator('-', "-", 4, Long::minus, Long::plus),
)

fun Char.isOperator() =
    operators.any { it.symbol == this }

fun getOperator(operator: Char) =
    operators.find { it.symbol == operator } ?:
        error("Bad operator $operator")

data class Operator(
    val symbol: Char,
    val regex: String,
    val precedence: Int,
    val operation: Operation,
    val opposite: Operation
){
    val expressionRegex =
        Regex("(-?\\d+) *$regex *(-?\\d+)")
}