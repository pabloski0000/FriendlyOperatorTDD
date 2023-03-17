package doubles

import application.Reader

class ReaderMock : Reader {
    var userDelay = 0L
    private var amountOfOperationsToResolve = 0

    fun readDelay(delay: Long) {
        userDelay = delay
    }

    fun readNumberOfOperationsToResolve(amount: Int) {
        amountOfOperationsToResolve = amount
    }

    override fun readDelay(): Long {
        return userDelay
    }

    override fun readNumberOfOperationsToDisplay(): Int {
        return amountOfOperationsToResolve
    }

}