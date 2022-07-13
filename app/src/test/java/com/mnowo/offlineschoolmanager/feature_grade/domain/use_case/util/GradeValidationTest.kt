package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import androidx.compose.ui.graphics.toArgb
import com.mnowo.offlineschoolmanager.core.theme.gradeOne
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.google.common.truth.Truth.assertThat
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.CalculateGradeColor
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.GradeResult
import org.junit.Test

class GradeValidationTest {

    @Test
    fun validGrade() {
        val grade = Grade(0, 0, "sdsdfsd", 2.0, true, gradeOne.toArgb())
        val result = GradeValidation.validateGrade(grade = grade)
        val roundedGrade = RoundOffDecimals.roundOffDoubleDecimals(grade = grade.grade)
        val color = CalculateGradeColor.calculateGradeColor(grade = grade.grade)
        assertThat(result).isEqualTo(
            GradeResult.Success(
                gradeColor = color,
                roundedGrade = roundedGrade
            )
        )
    }

    @Test
    fun emptyDescription() {
        val grade = Grade(0, 0, " ", 2.0, true, gradeOne.toArgb())
        val result = GradeValidation.validateGrade(grade = grade)
        assertThat(result).isEqualTo(
            GradeResult.EmptyDescription
        )
    }

    @Test
    fun gradeOutOfRange() {
        val grade = Grade(0, 0, "sdsdfsd", 0.0, true, gradeOne.toArgb())
        val result = GradeValidation.validateGrade(grade = grade)
        assertThat(result).isEqualTo(
            GradeResult.GradeOutOffRange
        )
    }


}