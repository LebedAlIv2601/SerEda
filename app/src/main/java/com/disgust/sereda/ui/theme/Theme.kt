package com.disgust.sereda.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//сделать цветовую палитру для темной темы
private val DarkColorPalette = darkColors(
    primary = Green300,
    primaryVariant = Green400
)

private val LightColorPalette = lightColors(
    primary = Green300,
    primaryVariant = Green400,
    secondary = Gray100,
    background = Color.White,
    surface = Gray100
)

@Composable
fun SerEdaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}