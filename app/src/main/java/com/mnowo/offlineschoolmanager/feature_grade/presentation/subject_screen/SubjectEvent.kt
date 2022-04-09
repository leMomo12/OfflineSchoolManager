package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject

sealed class SubjectEvent {
    object More: SubjectEvent()
    data class SubjectListData(val listData: List<Subject>) : SubjectEvent()
    data class OnSubjectClicked(val id: Int) : SubjectEvent()
}
