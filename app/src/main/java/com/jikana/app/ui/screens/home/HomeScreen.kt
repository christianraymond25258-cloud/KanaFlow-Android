package com.jikana.app.ui.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jikana.app.navigation.NavRoutes
import com.jikana.app.ui.theme.BackgroundCard
import com.jikana.app.ui.theme.BackgroundDark
import com.jikana.app.ui.theme.BackgroundElevated
import com.jikana.app.ui.theme.BorderGlow
import com.jikana.app.ui.theme.BorderSubtle
import com.jikana.app.ui.theme.SkyBlue
import com.jikana.app.ui.theme.SkyBlueDark
import com.jikana.app.ui.theme.SkyBlueLight
import com.jikana.app.ui.theme.TextMuted
import com.jikana.app.ui.theme.TextPrimary
import com.jikana.app.ui.theme.TextSecondary
import com.jikana.app.viewmodel.AuthViewModel
import com.jikana.app.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel = viewModel()
) {
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val headerAlpha = remember { Animatable(0f) }
    val headerOffset = remember { Animatable(30f) }
    val cardAlpha = remember { Animatable(0f) }
    val cardOffset = remember { Animatable(40f) }

    LaunchedEffect(Unit) {
        homeViewModel.refreshUser()
        launch {
            headerAlpha.animateTo(1f, tween(500))
        }
        launch {
            headerOffset.animateTo(0f, tween(500, easing = EaseOutCubic))
        }
        delay(200)
        launch {
            cardAlpha.animateTo(1f, tween(500))
        }
        launch {
            cardOffset.animateTo(0f, tween(500, easing = EaseOutCubic))
        }
    }

    LaunchedEffect(authState.user) {
        if (authState.user == null) {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.HOME) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        if (homeState.isLoading) {
            CircularProgressIndicator(
                color = SkyBlue,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 56.dp, bottom = 32.dp)
            ) {
                // Animated header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(headerAlpha.value)
                        .offset(y = headerOffset.value.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "こんにちは！",
                            fontSize = 13.sp,
                            color = SkyBlue,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = homeState.user?.name ?: "Learner",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    IconButton(
                        onClick = { authViewModel.signOut() },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(BackgroundCard)
                            .border(1.dp, BorderSubtle, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Sign Out",
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Animated cards section
                Column(
                    modifier = Modifier
                        .alpha(cardAlpha.value)
                        .offset(y = cardOffset.value.dp)
                ) {
                    StreakCard(streak = homeState.user?.streak ?: 0)

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Choose Your Practice",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Select a script to begin",
                        fontSize = 13.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModuleCard(
                        title = "Hiragana",
                        subtitle = "46 basic characters",
                        japaneseText = "ひらがな",
                        progress = homeState.user?.hiraganaProgress ?: 0,
                        totalRows = 10,
                        accentColor = SkyBlue,
                        onClick = { navController.navigate(NavRoutes.HIRAGANA) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ModuleCard(
                        title = "Katakana",
                        subtitle = "46 basic characters",
                        japaneseText = "カタカナ",
                        progress = homeState.user?.katakanaProgress ?: 0,
                        totalRows = 10,
                        accentColor = SkyBlueLight,
                        onClick = { navController.navigate(NavRoutes.KATAKANA) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ModuleCard(
                        title = "Kanji",
                        subtitle = "Common use characters",
                        japaneseText = "漢字",
                        progress = homeState.user?.kanjiProgress ?: 0,
                        totalRows = 20,
                        accentColor = SkyBlueDark,
                        onClick = { navController.navigate(NavRoutes.KANJI) }
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    OverallProgressCard(
                        hiragana = homeState.user?.hiraganaProgress ?: 0,
                        katakana = homeState.user?.katakanaProgress ?: 0,
                        kanji = homeState.user?.kanjiProgress ?: 0
                    )
                }
            }
        }
    }
}

@Composable
fun StreakCard(streak: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        SkyBlueDark.copy(alpha = 0.4f),
                        BackgroundCard
                    )
                )
            )
            .border(1.dp, BorderGlow, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Current Streak",
                    fontSize = 13.sp,
                    color = TextMuted
                )
                Text(
                    text = "$streak ${if (streak == 1) "day" else "days"}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (streak == 0) "Start learning today!"
                    else "Keep it going! 🔥",
                    fontSize = 12.sp,
                    color = SkyBlue
                )
            }
            Text(
                text = "🔥",
                fontSize = 48.sp
            )
        }
    }
}

@Composable
fun ModuleCard(
    title: String,
    subtitle: String,
    japaneseText: String,
    progress: Int,
    totalRows: Int,
    accentColor: Color,
    onClick: () -> Unit
) {
    val progressFraction = (progress.toFloat() / totalRows.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(BackgroundCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Text(
                    text = japaneseText,
                    fontSize = 28.sp,
                    color = accentColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$progress / $totalRows rows",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Text(
                    text = "${(progressFraction * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(BackgroundElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFraction)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    accentColor,
                                    accentColor.copy(alpha = 0.6f)
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun OverallProgressCard(hiragana: Int, katakana: Int, kanji: Int) {
    val total = ((hiragana / 10f) + (katakana / 10f) + (kanji / 20f)) / 3f
    val percentage = (total * 100).toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Overall Progress",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProgressStat(
                    label = "Hiragana",
                    value = "${hiragana * 10}%",
                    color = SkyBlue
                )
                ProgressStat(
                    label = "Katakana",
                    value = "${katakana * 10}%",
                    color = SkyBlueLight
                )
                ProgressStat(
                    label = "Kanji",
                    value = "${kanji * 5}%",
                    color = SkyBlueDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total Mastery: $percentage%",
                fontSize = 13.sp,
                color = SkyBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProgressStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}
