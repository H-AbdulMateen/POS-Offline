package com.abdulmateen.pos_offline.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),          // Bright blue (buttons, accents)
    onPrimary = Color.White,               // Text/icon on primary
    secondary = Color(0xFF90CAF9),         // Light blue highlights
    background = Color(0xFFF9FAFB),        // Page background (off-white)
    onBackground = Color(0xFF1A1A1A),      // Text color (near black)
    surface = Color(0xFFFFFFFF),           // Cards, panels, modals
    onSurface = Color(0xFF212121),         // Content text on surface
    outline = Color(0xFFE0E0E0),           // Divider or border
    surfaceVariant = Color(0xFFF2F4F7),    // Alternate section backgrounds
    error = Color(0xFFD32F2F),             // Error state (delete, invalid)
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF42A5F5),           // Lighter blue for dark mode accents
    onPrimary = Color.Black,               // Text/icon on primary
    secondary = Color(0xFF64B5F6),         // Slightly lighter secondary blue
    background = Color(0xFF121212),        // App background
    onBackground = Color(0xFFE0E0E0),      // Light gray text
    surface = Color(0xFF1A1A1A),           // Primary card/panel color
    onSurface = Color(0xFFF5F5F5),         // Text/icons on card

    surfaceVariant = Color(0xFF242424),    // Slightly lighter for nested cards
    outline = Color(0xFF333333),     // Variant surfaces (forms, panels)
    error = Color(0xFFEF5350),             // Error red
)

@Composable
fun extendedColor(light: Color, dark: Color): Color {
    return if(isSystemInDarkTheme()) dark else light
}

val ColorScheme.extraColor: Color @Composable get() = extendedColor(
    light = Color(0xFF000000),
    dark = Color(0xFFFFFFFF)
)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun POSOfflineTheme(
    content: @Composable () -> Unit,
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        shapes = Shapes,
        content = content
    )
}