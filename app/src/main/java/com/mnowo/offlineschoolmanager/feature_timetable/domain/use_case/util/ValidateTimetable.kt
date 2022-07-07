package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.util

import android.util.Log.d
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.TimetableResult


object ValidateTimetable {

    fun validateTimetable(timetable: Timetable, timetableList: List<Timetable>) : TimetableResult{
        if(timetable.day == Days.EXCEPTION) {
            return TimetableResult.EmptyDay
        }

        val result = timetableList.filter { (it.day == timetable.day) && (it.hour == timetable.hour) }

        if(result.isNotEmpty()) {
            return TimetableResult.AlreadyTaken
        }

        return TimetableResult.Success
    }
}