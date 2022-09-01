package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import android.util.Log.d
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.CalculateGradeColor
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.GradeResult
import kotlin.math.round

object GradeValidation {

    fun validateGrade(grade: Grade) : GradeResult {
        d("GradeResult", "grade grade: ${grade.grade}")
        try {
            if (grade.description.trim().isBlank()) {
                return GradeResult.EmptyDescription
            }
            val roundedGrade = RoundOffDecimals.roundOffDoubleDecimals(grade = grade.grade)

            val color = CalculateGradeColor.calculateGradeColor(grade = grade.grade)

            if (color == -1) {
                return GradeResult.GradeOutOffRange
            }
            return GradeResult.Success(gradeColor = color, roundedGrade = roundedGrade)
        } catch (e: Exception) {
            return GradeResult.Exception(message = e.localizedMessage)
        }
    }
}