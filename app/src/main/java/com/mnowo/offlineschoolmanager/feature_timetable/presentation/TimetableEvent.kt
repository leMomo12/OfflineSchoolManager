package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import androidx.compose.ui.graphics.Color
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable

sealed class TimetableEvent {
    data class OnHourPickerChanged(var hour: Int) : TimetableEvent()
    data class OnSubjectDialogStateChanged(var value: Boolean) : TimetableEvent()
    data class OnSubjectPicked(var subject: Subject) : TimetableEvent()
    data class OnSubjectDataReceived(var listData: List<Subject>) : TimetableEvent()
    data class OnPickedDayColorStateChanged(var day: Int) : TimetableEvent()
    data class SetPickSubjectError(var value : Color) : TimetableEvent()
    data class SetTimetableBottomSheet(var value: Boolean) : TimetableEvent()
    data class SetTimetableList(var listData: List<Timetable>) : TimetableEvent()
    data class SetAlreadyTakenErrorState(var value: Color) : TimetableEvent()
    object AddTimetable : TimetableEvent( )
}
