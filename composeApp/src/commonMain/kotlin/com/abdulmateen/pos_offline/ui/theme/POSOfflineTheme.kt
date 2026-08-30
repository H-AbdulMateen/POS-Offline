package com.abdulmateen.pos_offline.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    primaryFixed = Color.Green,
    onPrimaryFixed = Color.White
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
    primaryFixed = Color.Green,
    onPrimaryFixed = White

)

@Composable
fun extendedColor(light: Color, dark: Color): Color {
    return if(isSystemInDarkTheme()) dark else light
}

val ColorScheme.extraColor: Color @Composable get() = extendedColor(
    light = Color(0xFF000000),
    dark = Color(0xFFFFFFFF)
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(24.dp),
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
        typography = AppTypography,
        content = content
    )
}
