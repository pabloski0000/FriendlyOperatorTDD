import domain.Operator
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class OperatorTest {
    private val operator = Operator()

    @Test
    fun testOperateWhenTimeIsElapsed() {
        runBlocking {
            val beforeOperating = System.currentTimeMillis()
            val result = operator.multiply(8, 8, 4.5)
            val afterOperating = System.currentTimeMillis()
            val elapsedTimeInSeconds = (afterOperating - beforeOperating).toDouble() / 1000
            assertEquals(expected = 64, result)
            assert(elapsedTimeInSeconds in 4.0..5.0)
            println("Elapsed time: $elapsedTimeInSeconds")
        }
    }
}