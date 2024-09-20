package com.example.fetchrewardscodingexercise.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fetchrewardscodingexercise.presentation.theme.FetchRewardsCodingExerciseTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val mainActivityVM by viewModels<MainActivityVM>()



        setContent {
            FetchRewardsCodingExerciseTheme {

                val state = mainActivityVM.state.collectAsState()



                Scaffold(modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    when(state.value) {
                        is MainScreenState.Error -> {
                            Text(text = (state.value as MainScreenState.Error).message, modifier = Modifier.padding(innerPadding))
                        }
                        is MainScreenState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is MainScreenState.Success -> {
                            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                                items((state.value as MainScreenState.Success).data) {
                                    Text(text = it.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

