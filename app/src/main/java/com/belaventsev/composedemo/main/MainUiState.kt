package com.belaventsev.composedemo.main

import com.belaventsev.composedemo.Item
import java.lang.Exception

sealed class MainUiState {

    object Loading : MainUiState()

    data class Error(val exception: Exception) : MainUiState()

    data class Content(val items: List<Item>) : MainUiState()
}
