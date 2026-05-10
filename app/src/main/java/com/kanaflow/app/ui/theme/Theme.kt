package com.kanaflow.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KanaFlowColorScheme = darkColorScheme(
    // Primary — sky blue
    primary = SkyBlue,
    onPrimary = TextOnBlue,
    primaryContainer = SkyBlueDark,
    onPrimaryContainer = SkyBlueLight,

    // Secondary
    secondary = SkyBlueLight,
    onSecondary = TextOnBlue,
    secondaryContainer = BackgroundElevated,
    onSecondaryContainer = TextPrimary,

    // Backgrounds
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = BackgroundCard,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundElevated,
    onSurfaceVariant = TextSecondary,

    // States
    error = ErrorRed,
    onError = TextPrimary,

    // Outlines
    outline = BorderSubtle,
    outlineVariant = BorderGlow,
)

@Composable
fun KanaFlowTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = KanaFlowColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BackgroundDark.toArgb()
            window.navigationBarColor = BackgroundDark.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KanaFlowTypography,
        content = content
    )
}