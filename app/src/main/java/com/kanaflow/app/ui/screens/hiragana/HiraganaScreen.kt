package com.kanaflow.app.ui.screens.hiragana

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kanaflow.app.model.HiraganaData
import com.kanaflow.app.ui.theme.SkyBlue
import com.kanaflow.app.viewmodel.KanaViewModel

@Composable
fun HiraganaScreen(
    navController: NavController,
    kanaViewModel: KanaViewModel = viewModel(),
    currentProgress: Int = 0
) {
    var screen by remember { mutableStateOf("selector") }

    when (screen) {
        "selector" -> RowSelectorScreen(
            navController = navController,
            title = "Hiragana",
            scriptLabel = "ひらがな",
            rows = HiraganaData.rows,
            accentColor = SkyBlue,
            kanaViewModel = kanaViewModel,
            onStartPractice = { screen = "practice" }
        )
        "practice" -> PracticeScreen(
            navController = navController,
            accentColor = SkyBlue,
            kanaViewModel = kanaViewModel,
            onFinished = { screen = "results" }
        )
        "results" -> ResultsScreen(
            navController = navController,
            accentColor = SkyBlue,
            kanaViewModel = kanaViewModel,
            progressField = "hiraganaProgress",
            currentProgress = currentProgress
        )
    }
}