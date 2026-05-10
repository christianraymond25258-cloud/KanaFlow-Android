package com.kanaflow.app.ui.screens.hiragana

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kanaflow.app.navigation.NavRoutes
import com.kanaflow.app.ui.theme.BackgroundCard
import com.kanaflow.app.ui.theme.BackgroundDark
import com.kanaflow.app.ui.theme.BorderSubtle
import com.kanaflow.app.ui.theme.ErrorRed
import com.kanaflow.app.ui.theme.SuccessGreen
import com.kanaflow.app.ui.theme.TextMuted
import com.kanaflow.app.ui.theme.TextOnBlue
import com.kanaflow.app.ui.theme.TextPrimary
import com.kanaflow.app.ui.theme.TextSecondary
import com.kanaflow.app.viewmodel.KanaViewModel

@Composable
fun ResultsScreen(
    navController: NavController,
    accentColor: Color,
    kanaViewModel: KanaViewModel,
    progressField: String = "",
    currentProgress: Int = 0
) {
    val state by kanaViewModel.practiceState.collectAsStateWithLifecycle()
    val total = state.correctCount + state.wrongCount
    val percentage = if (total > 0) (state.correctCount * 100) / total else 0

    // Auto-save progress when screen loads
    LaunchedEffect(Unit) {
        if (progressField.isNotEmpty() && !state.progressSaved) {
            kanaViewModel.saveProgress(progressField, currentProgress)
        }
    }

    val emoji = when {
        percentage == 100 -> "🎉"
        percentage >= 80 -> "😊"
        percentage >= 60 -> "🙂"
        else -> "📚"
    }
    val message = when {
        percentage == 100 -> "Perfect Score!"
        percentage >= 80 -> "Great job!"
        percentage >= 60 -> "Good effort!"
        else -> "Keep practicing!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Spacer(modifier = Modifier.height(60.dp))

                Text(text = emoji, fontSize = 72.sp, textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Session Complete",
                    fontSize = 14.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                // Progress saved indicator
                if (state.progressSaved) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ Progress saved!",
                        fontSize = 13.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Score card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BackgroundCard)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$percentage%",
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${state.correctCount}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                                Text(text = "Correct", fontSize = 12.sp, color = TextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${state.wrongCount}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ErrorRed
                                )
                                Text(text = "Wrong", fontSize = 12.sp, color = TextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$total",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(text = "Total", fontSize = 12.sp, color = TextMuted)
                            }
                        }
                    }
                }

                if (state.wrongAnswers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Review Mistakes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            items(state.wrongAnswers) { wrongItem ->
                val kanaChar = wrongItem.first
                val userAnswer = wrongItem.second

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundCard)
                        .border(1.dp, ErrorRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = kanaChar.character,
                            fontSize = 32.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Correct: ${kanaChar.romaji}",
                                fontSize = 14.sp,
                                color = SuccessGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "You typed: $userAnswer",
                                fontSize = 13.sp,
                                color = ErrorRed
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                // Practice Again — go back to row selector
                Button(
                    onClick = {
                        kanaViewModel.resetPractice()
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) { inclusive = false }
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = TextOnBlue
                    )
                ) {
                    Text(
                        text = "Practice Again",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Back to Home — always navigates cleanly to home
                OutlinedButton(
                    onClick = {
                        kanaViewModel.resetPractice()
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextSecondary
                    )
                ) {
                    Text(
                        text = "Back to Home",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
