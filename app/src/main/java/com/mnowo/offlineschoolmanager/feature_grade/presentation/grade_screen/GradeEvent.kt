package com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen

import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade

sealed class GradeEvent {
    data class GradeListData(val listData: List<Grade>) : GradeEvent()
    data class EnteredClassTestDescription(val description: String) : GradeEvent()
    data class EnteredGrade(val grade: String) : GradeEvent()
    data class EnteredIsWritten(val isWritten: Boolean) : GradeEvent()
    object DeleteSpecificGrade : GradeEvent()
    object GetSpecificSubject : GradeEvent()
    object LoadGrades : GradeEvent()
    object AddGrade: GradeEvent()
    object UpdateGrade: GradeEvent()
    object NavBackToSubjectScreen: GradeEvent()
}
