import kotlin.test.Test
import kotlin.test.assertEquals

// place to add tests for util functions
class UtilsTest {

    @Test
    fun testStripInt(){
        assertEquals(0, "no int values".stripInt())
        assertEquals(5, "5 is an int at the start".stripInt())
        assertEquals(-8, "a negative int e.g. -8".stripInt())
        assertEquals(12, "several ints like 12 and 42".stripInt())
    }
}