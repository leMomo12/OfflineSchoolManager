package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import androidx.compose.ui.graphics.toArgb
import com.mnowo.offlineschoolmanager.core.theme.*

object CalculateGradeColor {

    fun calculateGradeColor(grade: Double): Int {
        when {
            grade > 0 && grade <= 1 -> {
                return gradeOne.toArgb()
            }
            grade > 1 && grade <= 1.5 -> {
                return gradeOneTwo.toArgb()
            }
            grade > 1.5 && grade <= 2.0 ->  {
                return gradeTwo.toArgb()
            }
            grade > 2.0 && grade <= 2.5 -> {
                return gradeTwoThree.toArgb()
            }
            grade > 2.5 && grade <= 3 -> {
                return gradeThree.toArgb()
            }
            grade > 3 && grade <= 3.5 -> {
                return gradeThreeFour.toArgb()
            }
            grade > 3.5 && grade <= 4.0 -> {
                return gradeFour.toArgb()
            }
            grade > 4.0 && grade <= 4.5 -> {
                return gradeFourFive.toArgb()
            }
            grade > 4.5 && grade <= 5.0 -> {
                return gradeFive.toArgb()
            }
            grade > 5 && grade <= 5.5 -> {
                return gradeFiveSix.toArgb()
            }
            grade > 5.5 && grade <= 6 -> {
                return gradeSix.toArgb()
            }
        }
        return -1
    }
}