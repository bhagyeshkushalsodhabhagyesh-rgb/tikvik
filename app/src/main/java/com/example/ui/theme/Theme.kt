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

private val ArtisticColorScheme = lightColorScheme(
    primary = ArtisticPrimary,
    onPrimary = Color.White,
    primaryContainer = ArtisticPrimaryContainer,
    onPrimaryContainer = ArtisticOnPrimaryContainer,
    secondary = ArtisticSecondary,
    onSecondary = ArtisticOnSecondary,
    secondaryContainer = ArtisticSecondary,
    onSecondaryContainer = ArtisticOnSecondary,
    tertiary = ArtisticTertiary,
    background = ArtisticBackground,
    onBackground = ArtisticOnBackground,
    surface = ArtisticSurface,
    onSurface = ArtisticOnSurface,
    surfaceVariant = ArtisticSurfaceVariant,
    onSurfaceVariant = ArtisticOnSurfaceVariant,
    outline = ArtisticOutline,
    outlineVariant = ArtisticOutlineVariant
)

private val DarkColorScheme =
  darkColorScheme(
    primary = Purple80, 
    secondary = PurpleGrey80, 
    tertiary = Pink80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF25232A),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
  )

private val LightColorScheme = ArtisticColorScheme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Default to false for the beautiful warm Artistic light layout
  dynamicColor: Boolean = false, // Disable dynamic colors to enforce the custom design theme
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> ArtisticColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
