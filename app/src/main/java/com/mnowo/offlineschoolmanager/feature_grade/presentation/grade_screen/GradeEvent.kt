package com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen

import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade

sealed class GradeEvent {
    data class GradeListData(val listData: List<Grade>) : GradeEvent()
    object LoadGrades : GradeEvent()
    object AddGrade: GradeEvent()
}
