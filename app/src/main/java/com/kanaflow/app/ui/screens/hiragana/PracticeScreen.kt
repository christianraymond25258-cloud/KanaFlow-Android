package com.kanaflow.app.ui.screens.hiragana

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kanaflow.app.model.AnswerState
import com.kanaflow.app.model.KanaChar
import com.kanaflow.app.ui.components.HapticFeedback
import com.kanaflow.app.ui.theme.BackgroundCard
import com.kanaflow.app.ui.theme.BackgroundDark
import com.kanaflow.app.ui.theme.BackgroundElevated
import com.kanaflow.app.ui.theme.BorderSubtle
import com.kanaflow.app.ui.theme.ErrorRed
import com.kanaflow.app.ui.theme.SuccessGreen
import com.kanaflow.app.ui.theme.TextMuted
import com.kanaflow.app.ui.theme.TextOnBlue
import com.kanaflow.app.ui.theme.TextPrimary
import com.kanaflow.app.ui.theme.TextSecondary
import com.kanaflow.app.viewmodel.KanaViewModel

@Composable
fun PracticeScreen(
    navController: NavController,
    accentColor: Color,
    kanaViewModel: KanaViewModel,
    onFinished: () -> Unit
) {
    val state by kanaViewModel.practiceState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    LaunchedEffect(state.currentIndex) {
        try { focusRequester.requestFocus() } catch (e: Exception) { }
    }

    LaunchedEffect(state.isFinished) {
        if (state.isFinished) onFinished()
    }

    LaunchedEffect(state.answerState) {
        when (state.answerState) {
            AnswerState.CORRECT -> HapticFeedback.correct(context)
            AnswerState.WRONG -> HapticFeedback.wrong(context)
            else -> {}
        }
    }

    val cardColor = when (state.answerState) {
        AnswerState.CORRECT -> SuccessGreen.copy(alpha = 0.15f)
        AnswerState.WRONG -> ErrorRed.copy(alpha = 0.15f)
        else -> BackgroundCard
    }
    val borderColor = when (state.answerState) {
        AnswerState.CORRECT -> SuccessGreen
        AnswerState.WRONG -> ErrorRed
        else -> BorderSubtle
    }
    val buttonColor = when (state.answerState) {
        AnswerState.CORRECT -> SuccessGreen
        AnswerState.WRONG -> ErrorRed
        else -> accentColor
    }
    val buttonText = when (state.answerState) {
        AnswerState.CORRECT -> "Next →"
        AnswerState.WRONG -> "Next →"
        else -> "Check Answer"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .imePadding(), // 👈 this is the key — pushes everything above keyboard
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    kanaViewModel.resetPractice()
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                val progress = if (state.totalChars > 0)
                    state.currentIndex.toFloat() / state.totalChars.toFloat() else 0f

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
                    text = "${state.currentIndex + 1}/${state.totalChars}",
                    fontSize = 13.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Score row
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

            // Animated character card
            AnimatedContent(
                targetState = state.currentChar,
                transitionSpec = {
                    (slideInHorizontally(
                        animationSpec = tween(300)
                    ) { it } + fadeIn(tween(300))).togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(300)
                        ) { -it } + fadeOut(tween(300))
                    )
                },
                label = "kana_card"
            ) { targetChar: KanaChar? ->
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(cardColor)
                        .border(2.dp, borderColor, RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.1f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Text(
                        text = targetChar?.character ?: "",
                        fontSize = 90.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Feedback text
            val feedbackText = when (state.answerState) {
                AnswerState.CORRECT -> "✓ Correct!"
                AnswerState.WRONG -> {
                    val r = state.currentChar?.romaji ?: ""
                    "✗ Wrong — it's \"$r\""
                }
                else -> ""
            }
            val feedbackColor = when (state.answerState) {
                AnswerState.CORRECT -> SuccessGreen
                AnswerState.WRONG -> ErrorRed
                else -> Color.Transparent
            }

            Text(
                text = feedbackText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = feedbackColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(22.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input field
            OutlinedTextField(
                value = state.input,
                onValueChange = { newValue ->
                    if (state.answerState == AnswerState.IDLE) {
                        kanaViewModel.onInputChanged(newValue)
                    }
                },
                label = { Text("Type the romaji...", color = TextMuted) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = accentColor,
                    focusedContainerColor = BackgroundCard,
                    unfocusedContainerColor = BackgroundCard
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (state.answerState == AnswerState.IDLE) {
                            kanaViewModel.submitAnswer()
                        } else {
                            kanaViewModel.nextCharacter()
                        }
                    }
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Button sits right above keyboard
            Button(
                onClick = {
                    if (state.answerState == AnswerState.IDLE) {
                        kanaViewModel.submitAnswer()
                    } else {
                        kanaViewModel.nextCharacter()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = TextOnBlue
                )
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
