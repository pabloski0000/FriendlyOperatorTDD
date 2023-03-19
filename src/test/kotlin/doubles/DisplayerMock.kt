package doubles

import Clock
import application.Displayer

class DisplayerMock : Displayer {
    private val clock = Clock()
    private enum class ThingsThatCanBeDisplayed {
        QUESTION,
        OPERATION,
    }
    private var thingsThatHaveBeenDisplayedInComingOrder = mutableListOf<ThingsThatCanBeDisplayed>()
    private var allOperationsHaveCompliedWithTheDelay = true
    private var minimumAllowedDelay = 0L
    private var maximumAllowedDelay = 0L
    private val displayedOperations = mutableListOf<Pair<Double, Double>>()

    fun initializationQuestionsAndOperationsAreDisplayedAndInAppropriateOrder(): Boolean {
        return initializationQuestionsAreTheFirstDisplayedThings() && thereIsAtLeastOneOperationAfterTheInitializationQuestions()
    }

    fun getNumberOfDisplayedOperations(): Int {
        return thingsThatHaveBeenDisplayedInComingOrder.filter { it == ThingsThatCanBeDisplayed.OPERATION }.size
    }

    fun getDisplayedOperationsAsString(): List<String> {
        return displayedOperations.map {
            "${it.first}*${it.second}"
        }
    }

    fun delayHasBeenCompliedThroughoutTheGame(): Boolean {
        return allOperationsHaveCompliedWithTheDelay
    }

    fun setOperationDelay(minimum: Long, maximum: Long) {
        minimumAllowedDelay = minimum
        maximumAllowedDelay = maximum
    }

    private fun initializationQuestionsAreTheFirstDisplayedThings(): Boolean {
        return containsAQuestionAtPosition(0) && containsAQuestionAtPosition(1)
    }

    private fun containsAQuestionAtPosition(position: Int): Boolean {
        return thingsThatHaveBeenDisplayedInComingOrder[position] == ThingsThatCanBeDisplayed.QUESTION
    }

    private fun thereIsAtLeastOneOperationAfterTheInitializationQuestions(): Boolean {
        return thingsThatHaveBeenDisplayedInComingOrder
            .subList(1, thingsThatHaveBeenDisplayedInComingOrder.size)
            .contains(ThingsThatCanBeDisplayed.OPERATION)
    }

    private fun operationHasNotCompliedWithDelay(): Boolean {
        return clock.getElapsedTime() !in minimumAllowedDelay..maximumAllowedDelay
    }

    private fun notifyIfOperationHasNotBeenDisplayedInTime() {
        if (isThereAnyPreviouslyDisplayedOperation()) {
            if (operationHasNotCompliedWithDelay()) {
                allOperationsHaveCompliedWithTheDelay = false
            }
        }
        clock.start()
    }

    private fun isThereAnyPreviouslyDisplayedOperation(): Boolean {
        return thingsThatHaveBeenDisplayedInComingOrder.contains(ThingsThatCanBeDisplayed.OPERATION)
    }

    override fun displayQuestion(question: String) {
        thingsThatHaveBeenDisplayedInComingOrder.add(ThingsThatCanBeDisplayed.QUESTION)
    }

    override fun displayMultiplication(firstOperand: Double, secondOperand: Double) {
        notifyIfOperationHasNotBeenDisplayedInTime()
        thingsThatHaveBeenDisplayedInComingOrder.add(ThingsThatCanBeDisplayed.OPERATION)
        displayedOperations.add(firstOperand to secondOperand)
    }
}
