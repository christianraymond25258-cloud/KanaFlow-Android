package com.jikana.app.ui.screens.kanji

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jikana.app.viewmodel.KanjiViewModel

@Composable
fun KanjiScreen(
    navController: NavController,
    kanjiViewModel: KanjiViewModel = viewModel()
) {
    var screen by remember { mutableStateOf("selector") }

    when (screen) {
        "selector" -> KanjiLevelSelector(
            navController = navController,
            kanjiViewModel = kanjiViewModel,
            onStartSession = { screen = "practice" }
        )
        "practice" -> KanjiPracticeScreen(
            navController = navController,
            kanjiViewModel = kanjiViewModel,
            onFinished = { screen = "results" }
        )
        "results" -> KanjiResultsScreen(
            navController = navController,
            kanjiViewModel = kanjiViewModel
        )
    }
}