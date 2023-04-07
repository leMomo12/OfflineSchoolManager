package com.mnowo.offlineschoolmanager.core.feature_core.domain.util

import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.mnowo.offlineschoolmanager.core.theme.*

object CalculateGradeColor {

    val MIN_GRADE = 0.7f
    val MAX_GRADE = 6.5f

    // Define the start and end colors (green and red)
    val START_COLOR = 0xFF008F00.toInt() // Darker green
    val END_COLOR = 0xFF8F0000.toInt() // Darker red

    fun calculateGradeColor(grade: Double): Int {
        val floatGrade = grade.toFloat()
        val fraction = (floatGrade - MIN_GRADE) / (MAX_GRADE - MIN_GRADE)
        return ColorUtils.blendARGB(START_COLOR, END_COLOR, fraction)
    }
}