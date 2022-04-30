package com.mnowo.offlineschoolmanager.feature_grade.domain.models

sealed class GradeResult {
    object EmptyDescription : GradeResult()
    object GradeOutOffRange : GradeResult()
    data class Exception(val message: String? = null) : GradeResult()
    data class Success(val gradeColor: Int? = null, val roundedGrade: Double? = null) :
        GradeResult()
}
