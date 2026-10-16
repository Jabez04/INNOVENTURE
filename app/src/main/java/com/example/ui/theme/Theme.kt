package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkTealPrimary,
    onPrimary = Color(0xFF00382F),
    primaryContainer = DarkTealContainer,
    onPrimaryContainer = Color(0xFFA7F0E4),
    secondary = GreenIncome,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF00522B),
    onSecondaryContainer = Color(0xFF8CF8AC),
    error = Color(0xFFFFB4AB),
    background = DarkBackground,
    onBackground = Color(0xFFE1E3DF),
    surface = DarkSurface,
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC0C9C4)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = OnTealContainer,
    secondary = GreenIncome,
    onSecondary = Color.White,
    secondaryContainer = GreenIncomeLight,
    onSecondaryContainer = GreenIncomeDark,
    error = ExpenseRed,
    onError = Color.White,
    errorContainer = ExpenseRedLight,
    onErrorContainer = Color(0xFF410002),
    background = BackgroundLight,
    onBackground = Color(0xFF191C1B),
    surface = SurfaceLight,
    onSurface = Color(0xFF191C1B),
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF3F4945)
  )

@Composable
fun StreetLedgerTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Default to brand colors (Teal & Green)
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Backward-compatible alias
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = StreetLedgerTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
