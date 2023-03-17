import application.Game
import doubles.DisplayerMock
import doubles.ReaderMock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    }
}