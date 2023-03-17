class Clock {
    private var timeWhenIAmStartedInMilliseconds = 0L

    /**
     * Start or restart the clock in case it has been started before
     */
    fun start() {
        timeWhenIAmStartedInMilliseconds = System.currentTimeMillis()
    }

    fun getElapsedTime(): Long {
        return System.currentTimeMillis() - timeWhenIAmStartedInMilliseconds
    }
}