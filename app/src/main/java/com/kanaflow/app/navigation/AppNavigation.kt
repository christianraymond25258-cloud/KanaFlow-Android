package com.kanaflow.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kanaflow.app.ui.screens.SplashScreen
import com.kanaflow.app.ui.screens.auth.LoginScreen
import com.kanaflow.app.ui.screens.auth.RegisterScreen
import com.kanaflow.app.ui.screens.hiragana.HiraganaScreen
import com.kanaflow.app.ui.screens.home.HomeScreen
import com.kanaflow.app.ui.screens.kanji.KanjiScreen
import com.kanaflow.app.ui.screens.katakana.KatakanaScreen
import com.kanaflow.app.viewmodel.AuthViewModel
import com.kanaflow.app.viewmodel.HomeViewModel
import com.kanaflow.app.viewmodel.KanaViewModel
import com.kanaflow.app.viewmodel.KanjiViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val kanaViewModel: KanaViewModel = viewModel()
    val kanjiViewModel: KanjiViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()

    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(navController = navController)
        }
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(NavRoutes.HOME) {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel
            )
        }
        composable(NavRoutes.HIRAGANA) {
            HiraganaScreen(
                navController = navController,
                kanaViewModel = kanaViewModel,
                currentProgress = homeState.user?.hiraganaProgress ?: 0
            )
        }
        composable(NavRoutes.KATAKANA) {
            KatakanaScreen(
                navController = navController,
                kanaViewModel = kanaViewModel,
                currentProgress = homeState.user?.katakanaProgress ?: 0
            )
        }
        composable(NavRoutes.KANJI) {
            KanjiScreen(
                navController = navController,
                kanjiViewModel = kanjiViewModel
            )
        }
    }
}