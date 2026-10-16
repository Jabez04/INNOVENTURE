package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SphereColorScheme =
  darkColorScheme(
    primary = CosmicPurplePrimary,
    onPrimary = TextWhite,
    primaryContainer = CosmicPurpleDeep,
    onPrimaryContainer = CosmicPurpleBright,
    secondary = NeonMint,
    onSecondary = NeonMintDark,
    secondaryContainer = NeonMintGlow,
    onSecondaryContainer = NeonMint,
    tertiary = CosmicPurpleGlow,
    background = ObsidianBackground,
    onBackground = TextWhite,
    surface = ObsidianSurface,
    onSurface = TextWhite,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = TextLavender,
    error = ExpensePink,
    onError = TextWhite
  )

@Composable
fun StreetLedgerTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = SphereColorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = StreetLedgerTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
