package com.kanaflow.app.ui.screens.katakana

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kanaflow.app.model.KatakanaData
import com.kanaflow.app.ui.screens.hiragana.PracticeScreen
import com.kanaflow.app.ui.screens.hiragana.ResultsScreen
import com.kanaflow.app.ui.screens.hiragana.RowSelectorScreen
import com.kanaflow.app.ui.theme.SkyBlueLight
import com.kanaflow.app.viewmodel.KanaViewModel

@Composable
fun KatakanaScreen(
    navController: NavController,
    kanaViewModel: KanaViewModel = viewModel(),
    currentProgress: Int = 0
) {
    var screen by remember { mutableStateOf("selector") }

    when (screen) {
        "selector" -> RowSelectorScreen(
            navController = navController,
            title = "Katakana",
            scriptLabel = "カタカナ",
            rows = KatakanaData.rows,
            accentColor = SkyBlueLight,
            kanaViewModel = kanaViewModel,
            onStartPractice = { screen = "practice" }
        )
        "practice" -> PracticeScreen(
            navController = navController,
            accentColor = SkyBlueLight,
            kanaViewModel = kanaViewModel,
            onFinished = { screen = "results" }
        )
        "results" -> ResultsScreen(
            navController = navController,
            accentColor = SkyBlueLight,
            kanaViewModel = kanaViewModel,
            progressField = "katakanaProgress",
            currentProgress = currentProgress
        )
    }
}