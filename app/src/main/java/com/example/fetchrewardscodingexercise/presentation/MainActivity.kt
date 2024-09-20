package com.example.fetchrewardscodingexercise.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.fetchrewardscodingexercise.presentation.composable.MainScreen
import com.example.fetchrewardscodingexercise.presentation.theme.FetchRewardsCodingExerciseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityVM by viewModels<MainActivityVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchRewardsCodingExerciseTheme {
                val state by mainActivityVM.state.collectAsState()
                MainScreen(
                    state = state,
                    onRefresh = { mainActivityVM.handleUserAction(UserAction.Refresh) },
                    onFilterTextChanged = { newText ->
                        mainActivityVM.handleUserAction(UserAction.Filter(newText))
                    }
                )
            }
        }
    }
}



