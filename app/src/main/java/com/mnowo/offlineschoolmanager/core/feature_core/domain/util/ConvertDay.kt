package com.mnowo.offlineschoolmanager.core.feature_core.domain.util

import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days

object ConvertDay {

    fun convertDayToInt(day: Days): Int {
        return when (day) {
            Days.MONDAY -> 0
            Days.TUESDAY -> 1
            Days.WEDNESDAY -> 2
            Days.THURSDAY -> 3
            Days.FRIDAY -> 4
            else -> 5
        }
    }

    fun convertIntToDay(day: Int): Days {
        return when (day) {
            0 -> Days.MONDAY
            1 -> Days.TUESDAY
            2 -> Days.WEDNESDAY
            3 -> Days.THURSDAY
            else -> Days.FRIDAY
        }
    }
}