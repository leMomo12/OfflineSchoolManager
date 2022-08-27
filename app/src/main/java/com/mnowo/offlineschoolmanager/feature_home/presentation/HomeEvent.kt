package com.mnowo.offlineschoolmanager.feature_home.presentation

import androidx.compose.ui.graphics.Color
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable

sealed class HomeEvent {
    data class SetAverageState(val value: Double) : HomeEvent()
    data class SetGradeColorState(val color: Color) : HomeEvent()
    data class SetTimetableListState(val listData: List<Timetable>) : HomeEvent()
    data class AddDailyTimetableListState(var listData: List<Timetable>) : HomeEvent()
    data class SetSubjectListState(var listData: List<Subject>) : HomeEvent()
    data class SetIsTodayTimetableState(var value: Boolean) : HomeEvent()
    data class SetEmptyDailyList(var isEmpty: Boolean) : HomeEvent()

}
