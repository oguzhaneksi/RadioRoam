package com.radioroam.android.paginator

/**
 * Paginator interface that defines the methods for loading the next items and resetting the paginator.
 *
 * @param Key The type of the key used for pagination.
 * @param Item The type of the items being paginated.
 */
interface Paginator<Key, Item> {
    /**
     * Load the next items in the pagination sequence.
     *
     * This method is a suspending function, which means it can be paused and resumed at later times.
     * It does not block the thread where it's running and only suspends the coroutine that it's being called from.
     */
    suspend fun loadNextItems()

    /**
     * Reset the paginator to its initial state.
     */
    fun reset()
}