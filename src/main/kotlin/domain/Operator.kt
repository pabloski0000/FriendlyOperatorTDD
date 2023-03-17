package domain

import kotlinx.coroutines.*

class Operator {
    private val coroutineScopeOnMainThread = CoroutineScope(Dispatchers.Main)

    suspend fun multiply(firstOperand: Int, secondOperand: Int, delayInSeconds: Double): Int {
        delay((delayInSeconds * 1000).toLong())
        return firstOperand * secondOperand
    }
}