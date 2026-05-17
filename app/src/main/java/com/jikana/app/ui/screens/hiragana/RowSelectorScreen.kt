package com.jikana.app.ui.screens.hiragana

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
import com.jikana.app.model.KanaRow
import com.jikana.app.ui.theme.BackgroundCard
import com.jikana.app.ui.theme.BackgroundDark
import com.jikana.app.ui.theme.BorderSubtle
import com.jikana.app.ui.theme.TextMuted
import com.jikana.app.ui.theme.TextOnBlue
import com.jikana.app.ui.theme.TextPrimary
import com.jikana.app.ui.theme.TextSecondary
import com.jikana.app.viewmodel.KanaViewModel

@Composable
fun RowSelectorScreen(
    navController: NavController,
    title: String,
    scriptLabel: String,
    rows: List<KanaRow>,
    accentColor: Color,
    kanaViewModel: KanaViewModel,
    onStartPractice: () -> Unit
) {
    val selectedRows = remember { mutableStateListOf<Int>() }

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
                                accentColor.copy(alpha = 0.12f),
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
                        text = scriptLabel,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Select rows to practice",
                        fontSize = 14.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Select All / Clear
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    selectedRows.clear()
                    selectedRows.addAll(rows.indices)
                }) {
                    Text("Select All", color = accentColor, fontSize = 13.sp)
                }
                TextButton(onClick = { selectedRows.clear() }) {
                    Text("Clear", color = TextMuted, fontSize = 13.sp)
                }
            }

            // Row list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(rows) { index, row ->
                    val isSelected = selectedRows.contains(index)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) accentColor.copy(alpha = 0.12f)
                                else BackgroundCard
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (isSelected) accentColor else BorderSubtle,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                if (isSelected) selectedRows.remove(index)
                                else selectedRows.add(index)
                            }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = row.rowName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) accentColor else TextPrimary
                                )
                                Text(
                                    text = row.characters.joinToString(" ") { it.character },
                                    fontSize = 18.sp,
                                    color = if (isSelected) accentColor.copy(alpha = 0.8f)
                                    else TextSecondary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (isSelected) selectedRows.remove(index)
                                    else selectedRows.add(index)
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

        // Start button pinned to bottom
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
                    if (selectedRows.isNotEmpty()) {
                        val selected = selectedRows.sorted().map { rows[it] }
                        kanaViewModel.startPractice(selected)
                        onStartPractice()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedRows.isNotEmpty()) accentColor else BorderSubtle,
                    contentColor = if (selectedRows.isNotEmpty()) TextOnBlue else TextMuted
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedRows.isEmpty()) "Select at least one row"
                    else "Start Practice (${selectedRows.size} rows)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
