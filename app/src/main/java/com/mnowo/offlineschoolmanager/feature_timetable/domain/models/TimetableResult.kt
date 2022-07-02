package com.mnowo.offlineschoolmanager.feature_timetable.domain.models

import com.mnowo.offlineschoolmanager.feature_timetable.presentation.TimetableEvent

sealed class TimetableResult {
    object EmptyDay : TimetableResult()
    object Success : TimetableResult()
}
