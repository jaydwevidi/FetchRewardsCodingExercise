package com.example.fetchrewardscodingexercise.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.presentation.MainScreenState


@Composable
fun FilterTextField(filterText: String, onFilterTextChanged: (String) -> Unit) {
    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(24.dp)
            )
        },
        placeholder = { Text("Search") },
        value = filterText,
        onValueChange = onFilterTextChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true
    )
}


@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



@Composable
fun ItemRow(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${item.id}", style = MaterialTheme.typography.bodySmall)
            Text(text = "List ID: ${item.listId}", style = MaterialTheme.typography.bodySmall)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainScreenState,
    onRefresh: () -> Unit,
    onFilterTextChanged: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopAppBar(onRefresh = onRefresh)
        }
    ) { innerPadding ->
        when (state) {
            is MainScreenState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            is MainScreenState.Error -> ErrorScreen(message = state.message)
            is MainScreenState.Success -> SuccessContent(
                modifier = Modifier.padding(innerPadding),
                filterText = state.filterText,
                onFilterTextChanged = onFilterTextChanged,
                items = state.items
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(onRefresh: () -> Unit) {
    TopAppBar(
        title = { Text("Fetch Rewards") },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
    )
}


@Composable
fun SuccessContent(
    modifier: Modifier = Modifier,
    filterText: String,
    onFilterTextChanged: (String) -> Unit,
    items: List<Item>
) {
    val groupedItems : Map<Int, List<Item>> = items.groupBy { it.listId }

    Column(modifier = modifier) {
        FilterTextField(
            filterText = filterText,
            onFilterTextChanged = onFilterTextChanged
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            groupedItems.forEach { (listId, itemsInGroup) ->
                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "List ID: $listId",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 30.sp // Adjust size to make the text bigger
                                ),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
                items(itemsInGroup) { item ->
                    ItemRow(item)
                }
            }
        }
    }
}