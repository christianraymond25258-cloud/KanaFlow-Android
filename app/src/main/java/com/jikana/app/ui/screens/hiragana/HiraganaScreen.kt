package com.jikana.app.ui.screens.hiragana

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jikana.app.model.HiraganaData
import com.jikana.app.ui.theme.SkyBlue
import com.jikana.app.viewmodel.KanaViewModel

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