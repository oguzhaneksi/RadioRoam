package com.radioroam.android.paginator

/**
 * Default implementation of the Paginator interface.
 *
 * @param Key The type of the key used for pagination.
 * @param Item The type of the items being paginated.
 * @property initialKey The initial key used for pagination.
 * @property onLoadUpdated Function to be called when the loading state is updated.
 * @property onRequest Function to be called to request the next items.
 * @property getNextKey Function to be called to get the next key for pagination.
 * @property onError Function to be called when an error occurs.
 * @property onSuccess Function to be called when items are successfully loaded.
 */
class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    /**
     * Load the next items in the pagination sequence.
     *
     * This method is a suspending function, which means it can be paused and resumed at later times.
     * It does not block the thread where it's running and only suspends the coroutine that it's being called from.
     */
    override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    /**
     * Reset the paginator to its initial state.
     */
    override fun reset() {
        currentKey = initialKey
    }
}