import common.expression.Expression
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpressionTest {

    private val validExpressions = mapOf(
        "5 * 10" to 50L,
        "-4 * 9 * 11" to -396L,
        "11+1*6" to 17L,
        "(17+3)*-8/2" to -80L,
        "((-3+4)*(3+4)+101)*2" to 216L
    )

    @Test
    fun testValidExpressionsWithoutVariables(){
        validExpressions.forEach { (exp, ans) ->
            val expression = Expression(exp)
            assertEquals(ans, expression.evaluate(), "Expression: $exp")
        }
    }

    @Test
    fun testValidExpressionWithMultipleVariables(){
        val expression = Expression("(x+11*2)/(y+1)-4")
        assertEquals(1,
            expression.evaluate("x" to -1, "y" to 3))
        assertEquals(-35,
            expression.evaluate("x" to 9, "y" to -2))
    }
}