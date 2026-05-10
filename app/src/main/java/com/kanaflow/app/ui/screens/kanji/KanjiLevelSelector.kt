package com.kanaflow.app.ui.screens.kanji

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kanaflow.app.model.KanjiData
import com.kanaflow.app.ui.theme.BackgroundCard
import com.kanaflow.app.ui.theme.BackgroundDark
import com.kanaflow.app.ui.theme.BorderSubtle
import com.kanaflow.app.ui.theme.SkyBlueDark
import com.kanaflow.app.ui.theme.TextMuted
import com.kanaflow.app.ui.theme.TextOnBlue
import com.kanaflow.app.ui.theme.TextPrimary
import com.kanaflow.app.ui.theme.TextSecondary
import com.kanaflow.app.viewmodel.KanjiViewModel

@Composable
fun KanjiLevelSelector(
    navController: NavController,
    kanjiViewModel: KanjiViewModel,
    onStartSession: () -> Unit
) {
    val selectedLevels = remember { mutableStateListOf<Int>() }
    val accentColor = SkyBlueDark

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.15f),
                                BackgroundDark
                            )
                        )
                    )
                    .padding(top = 52.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
            ) {
                Column {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.offset(x = (-12).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "漢字",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = "Kanji",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Choose levels to study",
                        fontSize = 14.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // SRS explanation card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.08f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "🧠  Multiple choice — select the correct meaning for each kanji shown",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }

            // Select All / Clear
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    selectedLevels.clear()
                    selectedLevels.addAll(KanjiData.allLevels.indices)
                }) {
                    Text("Select All", color = accentColor, fontSize = 13.sp)
                }
                TextButton(onClick = { selectedLevels.clear() }) {
                    Text("Clear", color = TextMuted, fontSize = 13.sp)
                }
            }

            // Level list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(KanjiData.allLevels) { index, levelPair ->
                    val levelName = levelPair.first
                    val levelCards = levelPair.second
                    val isSelected = selectedLevels.contains(index)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) accentColor.copy(alpha = 0.1f)
                                else BackgroundCard
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (isSelected) accentColor else BorderSubtle,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                if (isSelected) selectedLevels.remove(index)
                                else selectedLevels.add(index)
                            }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = levelName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) accentColor else TextPrimary
                                )
                                Text(
                                    text = levelCards.joinToString(" ") { it.kanji },
                                    fontSize = 20.sp,
                                    color = if (isSelected) accentColor.copy(alpha = 0.8f)
                                    else TextSecondary,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                                Text(
                                    text = "${levelCards.size} cards",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (isSelected) selectedLevels.remove(index)
                                    else selectedLevels.add(index)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = accentColor,
                                    uncheckedColor = BorderSubtle,
                                    checkmarkColor = BackgroundDark
                                )
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        // Start button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, BackgroundDark)
                    )
                )
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    if (selectedLevels.isNotEmpty()) {
                        kanjiViewModel.startSession(selectedLevels.sorted())
                        onStartSession()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedLevels.isNotEmpty()) accentColor
                    else BorderSubtle,
                    contentColor = if (selectedLevels.isNotEmpty()) TextOnBlue
                    else TextMuted
                )
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedLevels.isEmpty()) "Select at least one level"
                    else "Start Session (${selectedLevels.size} levels)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}