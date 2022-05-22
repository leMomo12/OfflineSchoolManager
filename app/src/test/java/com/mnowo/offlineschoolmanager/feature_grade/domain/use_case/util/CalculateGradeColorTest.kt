package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import androidx.compose.ui.graphics.toArgb
import com.google.common.truth.Truth.assertThat
import com.mnowo.offlineschoolmanager.core.theme.*
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import org.junit.Test

class CalculateGradeColorTest {

    @Test
    fun gradeOutOfRange() {
        val result = CalculateGradeColor.calculateGradeColor(grade = 0.7)
        val result2 = CalculateGradeColor.calculateGradeColor(6.6)
        assertThat(result).isEqualTo(-1)
        assertThat(result2).isEqualTo(-1)
    }

    @Test
    fun `grade between 0_75 and 1`() {
        val result = CalculateGradeColor.calculateGradeColor(0.8)
        assertThat(result).isEqualTo(gradeOne.toArgb())
    }

    @Test
    fun `grade between 1 and 1_5`() {
        val result = CalculateGradeColor.calculateGradeColor(1.3)
        assertThat(result).isEqualTo(gradeOneTwo.toArgb())
    }

    @Test
    fun `grade between 1_5 and 2`() {
        val result = CalculateGradeColor.calculateGradeColor(1.6)
        assertThat(result).isEqualTo(gradeTwo.toArgb())
    }

    @Test
    fun `grade between 2 and 2_5`() {
        val result = CalculateGradeColor.calculateGradeColor(2.4)
        assertThat(result).isEqualTo(gradeTwoThree.toArgb())
    }

    @Test
    fun `grade between 2_5 and 3`() {
        val result = CalculateGradeColor.calculateGradeColor(2.6)
        assertThat(result).isEqualTo(gradeThree.toArgb())
    }

    @Test
    fun `grade between 3 and 3_5`() {
        val result = CalculateGradeColor.calculateGradeColor(3.4)
        assertThat(result).isEqualTo(gradeThreeFour.toArgb())
    }

    @Test
    fun `grade between 3_5 and 4`() {
        val result = CalculateGradeColor.calculateGradeColor(3.7)
        assertThat(result).isEqualTo(gradeFour.toArgb())
    }

    @Test
    fun `grade between 4 and 4_5`() {
        val result = CalculateGradeColor.calculateGradeColor(4.1)
        assertThat(result).isEqualTo(gradeFourFive.toArgb())
    }

    @Test
    fun `grade between 4_5 and 5`() {
        val result = CalculateGradeColor.calculateGradeColor(4.645)
        assertThat(result).isEqualTo(gradeFive.toArgb())
    }

    @Test
    fun `grade between 5 and 5_5`() {
        val result = CalculateGradeColor.calculateGradeColor(5.45)
        assertThat(result).isEqualTo(gradeFiveSix.toArgb())
    }

    @Test
    fun `grade between 5_5 and 6_5`() {
        val result = CalculateGradeColor.calculateGradeColor(6.5)
        assertThat(result).isEqualTo(gradeSix.toArgb())
    }
}