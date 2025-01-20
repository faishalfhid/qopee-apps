package com.dicoding.tugasakhircompose.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = LightCoffeeBrown,
    secondary = LightCoffeeBrown,
    secondaryContainer = LightCoffeeBrown,
    onSecondaryContainer = CoffeeBrown,
    onSurface = LightCoffeeBrown,
    onSurfaceVariant = LightCoffeeBrown
)

private val LightColorScheme = lightColorScheme(
    primary = CoffeeBrown,
    secondary = CoffeeBrown,
    secondaryContainer = LightCoffeeBrown,
    onSecondaryContainer = CoffeeBrown,
    onSurface = CoffeeBrown,
    onSurfaceVariant = LightGray,
    background = Color.White
)

@Composable
fun TugasAkhirComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}