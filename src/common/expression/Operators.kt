package common.expression

typealias Operation = (Long, Long) -> Long

val operators = listOf(
    Operator('*', "\\*", 1, Long::times),
    Operator('/', "/", 2, Long::div),
    Operator('+', "\\+", 3, Long::plus),
    Operator('-', "-", 4, Long::minus),
)

fun getOperator(operator: Char) =
    operators.find { it.symbol == operator } ?:
        error("Bad operator $operator")

data class Operator(
    val symbol: Char,
    val regex: String,
    val precedence: Int,
    val operation: Operation
){
    val expressionRegex =
        Regex("(-?\\d+) *$regex *(-?\\d+)")
}