package application

import kotlinx.coroutines.delay

class Game(
    private val displayer: Displayer,
    private val reader: Reader,
) {

    suspend fun start() {
        displayer.displayQuestion("Tell me the delay nigga")
        val delay = reader.readDelay()
        displayer.displayQuestion("Tell me the number of operations to resolve")
        val numberOfOperationsToDisplay = reader.readNumberOfOperationsToDisplay()
        for (i in 0 until numberOfOperationsToDisplay) {
            displayer.displayMultiplication(0.0, 0.0)
            delay(delay)
        }
    }
}