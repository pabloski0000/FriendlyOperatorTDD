import application.Game
import doubles.DisplayerMock
import doubles.ReaderMock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception
import kotlin.test.assertEquals

class GameTest {
    private val displayer = DisplayerMock()
    private val reader = ReaderMock()
    private val game = Game(displayer, reader)
    private val numberOfOperationsToSolveByDefault = 5

    @BeforeEach
    fun setUp() {
        reader.readDelay(0)
        reader.readNumberOfOperationsToResolve(numberOfOperationsToSolveByDefault)
    }

    @Test
    fun assertItFirstlyDisplaysQuestionAboutDelayAndSecondlyAnOperation() {
        runBlocking {
            game.start()
            assert(displayer.initializationQuestionsAndOperationsAreDisplayedAndInAppropriateOrder())
        }
    }

    @Test
    fun assertItPassesToTheNextOperationWhenDelayHasElapsed() {
        runBlocking {
            val delayBetweenOperations = 1000L
            reader.readDelay(delayBetweenOperations)
            displayer.setOperationDelay(
                minimum = delayBetweenOperations - 500,
                maximum = delayBetweenOperations + 500,
            )
            game.start()
            assert(
                displayer.delayHasBeenCompliedThroughoutTheGame()
            )
        }
    }

    @Test
    fun assertAllOperationsAreDisplayed() {
        runBlocking {
            val numberOfOperationsToDisplay = 30
            reader.readNumberOfOperationsToResolve(numberOfOperationsToDisplay)
            reader.readDelay(0)
            game.start()
            assertEquals(expected = numberOfOperationsToDisplay, displayer.getNumberOfDisplayedOperations())
        }
    }

    @Test
    fun assertOperationAreActuallyRandom() {
        runBlocking {
            val numberOfOperations = 1000
            reader.readNumberOfOperationsToResolve(numberOfOperations)
            reader.readDelay(0)
            game.start()
            val operations = displayer.getDisplayedOperationsAsString()
            val operationClassification = classifyOperations(operations)
            assertOperationsAreGeneratedRandomly(numberOfOperations, operationClassification)
        }
    }

    private fun classifyOperations(operations: List<String>): MutableMap<Pair<Int, Int>, Int> {
        val orderedPairs = extractOrderedPairsFromOperations(operations)
        return classifyOrderedPairs(orderedPairs)
    }

    private fun classifyOrderedPairs(orderedPairs: List<Pair<Int, Int>>): MutableMap<Pair<Int, Int>, Int> {
        val orderedPairTable = initialiseOrderedPairTable(
            minimumOperand = 2,
            maximumOperand = 9,
        )
        return fillOutTableWithOrderedPairs(orderedPairs, orderedPairTable)
    }

    private fun fillOutTableWithOrderedPairs(
        orderedPairs: List<Pair<Int, Int>>,
        table: MutableMap<Pair<Int, Int>, Int>,
    ): MutableMap<Pair<Int, Int>, Int> {
        orderedPairs.map { orderedPair ->
            table[orderedPair] = table[orderedPair]!! + 1
        }
        return table
    }

    private fun initialiseOrderedPairTable(
        minimumOperand: Int,
        maximumOperand: Int,
    ): MutableMap<Pair<Int, Int>, Int> {
        val operationClassification = mutableMapOf<Pair<Int, Int>, Int>()
        for (i in minimumOperand .. maximumOperand) {
            for (j in minimumOperand .. maximumOperand) {
                val orderedPair = Pair(i, j)
                operationClassification[orderedPair] = 0
            }
        }
        return operationClassification
    }

    private fun assertOperationsAreGeneratedRandomly(
        numberOfOperations: Int,
        operationClassification: MutableMap<Pair<Int, Int>, Int>,
    ) {
        val perfectPercentageOfAppearance = 100.0 / operationClassification.size
        val allowedError = 5
        val minimumAppearancePercentage = perfectPercentageOfAppearance - allowedError
        val maximumAppearancePercentage = perfectPercentageOfAppearance + allowedError
        for ((orderedPair, numberOfTimesThatHasAppeared) in operationClassification) {
            val orderedPairPercentageAppearance = numberOfTimesThatHasAppeared.toDouble() / numberOfOperations * 100
            if (orderedPairPercentageAppearance !in minimumAppearancePercentage..maximumAppearancePercentage) {
                throw Exception("The ordered pair $orderedPair appears $orderedPairPercentageAppearance%" +
                        " ($numberOfTimesThatHasAppeared times) but should appear between" +
                        " $minimumAppearancePercentage% (${minimumAppearancePercentage / 100 * numberOfOperations} times) and" +
                        " $maximumAppearancePercentage% (${maximumAppearancePercentage / 100 * numberOfOperations} times), both" +
                        " included")
            }
        }
    }

    private fun extractOrderedPairsFromOperations(operations: List<String>): List<Pair<Int, Int>> {
        val orderedPairsFromOperations = mutableListOf<Pair<Int, Int>>()
        operations.map { operation ->
            orderedPairsFromOperations.add(getOrderedPairFromAOperation(operation))
        }
        return orderedPairsFromOperations
    }

    private fun getOrderedPairFromAOperation(operation: String): Pair<Int, Int> {
        return Pair(operation[0].digitToInt(), operation[2].digitToInt())
    }
}