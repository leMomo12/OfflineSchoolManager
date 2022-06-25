package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject

sealed class TimetableEvent {
    data class OnHourPickerChanged(var hour: Int) : TimetableEvent()
    data class OnSubjectDialogStateChanged(var value: Boolean) : TimetableEvent()
    data class OnSubjectPicked(var subject: Subject) : TimetableEvent()
    data class OnSubjectDataReceived(var listData: List<Subject>) : TimetableEvent()
    data class OnPickedDayColorStateChanged(var day: Int) : TimetableEvent()
}
