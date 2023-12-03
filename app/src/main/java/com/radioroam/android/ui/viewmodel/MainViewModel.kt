package com.radioroam.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {

    private val _isPlayerSetUp = MutableStateFlow(false)
    val isPlayerSetUp = _isPlayerSetUp.asStateFlow()

    fun setupPlayer() {
        _isPlayerSetUp.update {
            true
        }
    }

}