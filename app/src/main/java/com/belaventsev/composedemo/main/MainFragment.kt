package com.belaventsev.composedemo.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.belaventsev.composedemo.Item
import com.belaventsev.composedemo.R
import com.belaventsev.composedemo.ui.theme.ComposeDemoTheme

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view as ComposeView) {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ComposeDemoTheme {
                    MainScreen(
                        uiState = viewModel.mainUiState.collectAsState().value,
                        onItemClick = { showToast(it.name) },
                        onRefreshClick = viewModel::onRepeatClick,
                        onCheckedChange = viewModel::onCheckedChange,
                    )
                }
            }
        }
    }

    private fun showToast(text: String) = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

    @Composable
    private fun MainScreen(
        uiState: MainUiState,
        onItemClick: (Item) -> Unit,
        onRefreshClick: () -> Unit,
        onCheckedChange: (Item, Boolean) -> Unit,
    ) = when (uiState) {
        is MainUiState.Content -> Items(uiState.items, onItemClick, onCheckedChange, onRefreshClick)
        is MainUiState.Error -> Error(uiState.exception, onRefreshClick)
        MainUiState.Loading -> ProgressIndicator()
    }

    @Composable
    private fun ProgressIndicator() = Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

    @Composable
    private fun Items(
        items: List<Item>,
        onItemClick: (Item) -> Unit,
        onCheckedChange: (Item, Boolean) -> Unit,
        onRefreshClick: () -> Unit
    ) = Box {
        LazyColumn {
            items(items) { item ->
                Item(
                    item = item,
                    onClick = { onItemClick(item) },
                    onCheckedChange = { checked -> onCheckedChange(item, checked) },
                )
            }
        }
        FloatingActionButton(
            onClick = onRefreshClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) { Icon(Icons.Default.Refresh, contentDescription = null) }
    }

    @Composable
    private fun Item(item: Item, onClick: () -> Unit, onCheckedChange: (Boolean) -> Unit) = Column(Modifier.clickable(onClick = onClick)) {
        Row(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp), Alignment.CenterVertically) {
            Checkbox(checked = item.checked, onCheckedChange)
            Text(text = item.name)
        }
        Divider()
    }

    @Composable
    private fun Error(e: Exception, onRefreshClick: () -> Unit) =
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.error), Modifier.padding(16.dp))
            Button(onRefreshClick) {
                Text(stringResource(R.string.button_repeat))
            }
        }
}