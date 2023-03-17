package application

interface Reader {
    fun readDelay(): Long
    fun readNumberOfOperationsToDisplay(): Int
}