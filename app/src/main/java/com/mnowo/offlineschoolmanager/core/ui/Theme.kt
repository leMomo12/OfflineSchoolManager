package com.mnowo.offlineschoolmanager.core.theme

import android.hardware.lights.Light
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Color.White,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Color.Black,
    primaryVariant = Color.Black,
    secondary = Color.Black,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,

)

@Composable
fun OfflineSchoolManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val systemUiController = rememberSystemUiController()

    val colors = if (darkTheme) {
        LightColorPalette
    } else {
        LightColorPalette
    }

    if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = Color.White
        )
    } else {
        systemUiController.setStatusBarColor(
            color = Color.White
        )
    }

    systemUiController.setNavigationBarColor(color = Color.White)

    MaterialTheme(
        colors = colors,
        typography = NotosansTypography,
        shapes = Shapes,
        content = content
    )
}