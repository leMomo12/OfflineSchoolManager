package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.util

import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.TimetableResult


object ValidateTimetable {

    fun validateTimetable(timetable: Timetable) : TimetableResult{
        if(timetable.day == Days.EXCEPTION) {
            return TimetableResult.EmptyDay
        }

        return TimetableResult.Success
    }
}