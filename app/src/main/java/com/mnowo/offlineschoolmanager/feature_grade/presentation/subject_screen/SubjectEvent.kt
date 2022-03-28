package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

sealed class SubjectEvent {
    object AddSubject: SubjectEvent()
    object More: SubjectEvent()
    object ColorPick : SubjectEvent()
    data class OnSubjectClicked(val id: Int) : SubjectEvent()
}
