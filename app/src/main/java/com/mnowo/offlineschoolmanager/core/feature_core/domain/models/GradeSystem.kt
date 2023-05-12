package com.mnowo.offlineschoolmanager.core.feature_core.domain.models

import java.util.*


abstract class GradeSystem(
    minGrade: Float,
    maxGrade: Float
) {
    abstract fun calculateAverageGradeColor(grades: Double): Int
}