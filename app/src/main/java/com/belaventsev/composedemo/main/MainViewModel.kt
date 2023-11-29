package com.belaventsev.composedemo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belaventsev.composedemo.Item
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MainViewModel : ViewModel() {

    private val items = listOf(
        Item(name = "Item 1", checked = false),
        Item(name = "Item 2", checked = false),
        Item(name = "Item 3", checked = false),
    )

    private val _mainUiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val mainUiState = _mainUiState.asStateFlow()

    init {
        loadData()
    }

    fun onRepeatClick() = loadData()

    fun onCheckedChange(item: Item, checked: Boolean) {
        _mainUiState.update { state ->
            val items = (state as MainUiState.Content).items
            val index = items.indexOf(item)
            MainUiState.Content(items.subList(0, index) + item.copy(checked = checked) + items.subList(index + 1, items.size))
        }
    }

    private fun loadData() = viewModelScope.launch {
        _mainUiState.value = MainUiState.Loading
        delay(2.seconds)
        _mainUiState.value = if (Random.nextBoolean()) MainUiState.Content(items) else MainUiState.Error(RuntimeException())
    }
}