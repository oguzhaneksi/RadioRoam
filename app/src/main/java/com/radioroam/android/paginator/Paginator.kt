package com.radioroam.android.paginator

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}