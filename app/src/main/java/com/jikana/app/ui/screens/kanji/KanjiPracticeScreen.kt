package com.jikana.app.ui.screens.kanji

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jikana.app.ui.theme.BackgroundCard
import com.jikana.app.ui.theme.BackgroundDark
import com.jikana.app.ui.theme.BackgroundElevated
import com.jikana.app.ui.theme.BorderSubtle
import com.jikana.app.ui.theme.ErrorRed
import com.jikana.app.ui.theme.SkyBlueDark
import com.jikana.app.ui.theme.SuccessGreen
import com.jikana.app.ui.theme.TextMuted
import com.jikana.app.ui.theme.TextOnBlue
import com.jikana.app.ui.theme.TextPrimary
import com.jikana.app.ui.theme.TextSecondary
import com.jikana.app.viewmodel.KanjiAnswerState
import com.jikana.app.viewmodel.KanjiViewModel

@Composable
fun KanjiPracticeScreen(
    navController: NavController,
    kanjiViewModel: KanjiViewModel,
    onFinished: () -> Unit
) {
    val state by kanjiViewModel.sessionState.collectAsStateWithLifecycle()
    val accentColor = SkyBlueDark

    LaunchedEffect(state.isFinished) {
        if (state.isFinished) onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    kanjiViewModel.resetSession()
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                val progress = if (state.totalCards > 0)
                    state.currentIndex.toFloat() / state.totalCards.toFloat() else 0f

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(BackgroundElevated)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(accentColor)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "${state.currentIndex + 1}/${state.totalCards}",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "✓ ${state.correctCount}",
                    fontSize = 14.sp,
                    color = SuccessGreen,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "✗ ${state.wrongCount}",
                    fontSize = 14.sp,
                    color = ErrorRed,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kanji card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(BackgroundCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Glow
                    Box(
                        modifier = Modifier
                            .height(160.dp)
                            .fillMaxWidth()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.12f),
                                        Color.Transparent
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val kanjiText = state.currentCard?.kanji ?: ""
                        Text(
                            text = kanjiText,
                            fontSize = 100.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "What does this mean?",
                        fontSize = 14.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )

                    // Show readings after answer
                    if (state.answerState != KanjiAnswerState.IDLE) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val onyomi = state.currentCard?.onyomi ?: ""
                        val kunyomi = state.currentCard?.kunyomi ?: ""
                        val example = state.currentCard?.example ?: ""
                        if (onyomi.isNotEmpty()) {
                            Text(
                                text = "音読み: $onyomi",
                                fontSize = 13.sp,
                                color = accentColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        if (kunyomi.isNotEmpty()) {
                            Text(
                                text = "訓読み: $kunyomi",
                                fontSize = 13.sp,
                                color = accentColor.copy(alpha = 0.7f)
                            )
                        }
                        if (example.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = example,
                                fontSize = 12.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Multiple choice options
            val options = state.options
            val chunked = options.chunked(2)

            chunked.forEach { rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowOptions.forEach { option ->
                        val isSelected = state.selectedOption == option
                        val correctMeaning = state.currentCard?.meaning ?: ""
                        val isCorrectOption = option == correctMeaning

                        val optionBgColor = when {
                            state.answerState == KanjiAnswerState.IDLE -> BackgroundCard
                            isCorrectOption -> SuccessGreen.copy(alpha = 0.2f)
                            isSelected && !isCorrectOption -> ErrorRed.copy(alpha = 0.2f)
                            else -> BackgroundCard
                        }
                        val optionBorderColor = when {
                            state.answerState == KanjiAnswerState.IDLE && isSelected ->
                                accentColor
                            isCorrectOption -> SuccessGreen
                            isSelected && !isCorrectOption -> ErrorRed
                            else -> BorderSubtle
                        }
                        val optionTextColor = when {
                            isCorrectOption && state.answerState != KanjiAnswerState.IDLE ->
                                SuccessGreen
                            isSelected && !isCorrectOption -> ErrorRed
                            else -> TextPrimary
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(optionBgColor)
                                .border(1.5.dp, optionBorderColor, RoundedCornerShape(14.dp))
                                .clickable(enabled = state.answerState == KanjiAnswerState.IDLE) {
                                    kanjiViewModel.selectAnswer(option)
                                }
                                .padding(vertical = 18.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = optionTextColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Next button — only shows after answering
            if (state.answerState != KanjiAnswerState.IDLE) {
                val nextBtnColor = when (state.answerState) {
                    KanjiAnswerState.CORRECT -> SuccessGreen
                    KanjiAnswerState.WRONG -> ErrorRed
                    else -> accentColor
                }
                Button(
                    onClick = { kanjiViewModel.nextCard() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = nextBtnColor,
                        contentColor = TextOnBlue
                    )
                ) {
                    Text(
                        text = "Next →",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}