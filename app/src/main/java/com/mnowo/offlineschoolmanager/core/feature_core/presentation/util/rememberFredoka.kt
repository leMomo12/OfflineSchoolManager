package com.mnowo.offlineschoolmanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Composable
fun rememberFredoka(): FontFamily {
    return FontFamily(
        Font(R.font.fredoka_light, FontWeight.Light),
        Font(R.font.fredoka_regular, FontWeight.Normal),
        Font(R.font.fredoka_medium, FontWeight.Medium),
        Font(R.font.fredoka_semibold, FontWeight.SemiBold),
        Font(R.font.fredoka_bold, FontWeight.Bold)
    )
}