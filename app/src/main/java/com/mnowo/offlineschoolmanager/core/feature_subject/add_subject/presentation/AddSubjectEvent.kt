package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation

sealed class AddSubjectEvent {
    data class EnteredSubject(val subject: String) : AddSubjectEvent()
    data class EnteredRoom(val room: String) : AddSubjectEvent()
    data class EnteredOralPercentage(val percentage: String) : AddSubjectEvent()
    data class EnteredWrittenPercentage(val percentage: String) : AddSubjectEvent()
    object PickedColor: AddSubjectEvent()
    object AddSubject : AddSubjectEvent()
}
