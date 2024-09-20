package com.example.fetchrewardscodingexercise.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.presentation.theme.FetchRewardsCodingExerciseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val mainActivityVM by viewModels<MainActivityVM>()

        setContent {
            FetchRewardsCodingExerciseTheme {
                val state by mainActivityVM.state.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Fetch Rewards") },
                            actions = {
                                IconButton(onClick = {
                                    mainActivityVM.handleUserAction(UserAction.Refresh)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    when (val currentState = state) {
                        is MainScreenState.Loading -> {
                            LoadingScreen()
                        }
                        is MainScreenState.Error -> {
                            ErrorScreen(message = currentState.message)
                        }
                        is MainScreenState.Success -> {
                            Column(modifier = Modifier.padding(innerPadding)) {
                                FilterTextField(
                                    filterText = currentState.filterText,
                                    onFilterTextChanged = { newText ->
                                        mainActivityVM.handleUserAction(
                                            UserAction.Filter(newText)
                                        )
                                    }
                                )
                                ItemList(items = currentState.items)
                            }
                        }
                    }
                }
            }
        }
    }
}

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
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search,
            keyboardType = KeyboardType.Number
        )
    )
}



@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
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
fun ItemList(items: List<Item>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { item ->
            ItemRow(item)
        }
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