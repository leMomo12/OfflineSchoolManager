package com.mnowo.offlineschoolmanager.feature_home.presentation

import androidx.compose.ui.graphics.Color
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable

sealed class HomeEvent {
    data class SetAverageState(val value: Double) : HomeEvent()
    data class SetGradeColorState(val color: Color) : HomeEvent()
    data class SetTimetableListState(val listData: List<Timetable>) : HomeEvent()
}
