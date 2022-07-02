package com.mnowo.offlineschoolmanager.feature_timetable.domain.models

enum class Days(val day: Int) {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    EXCEPTION(5) // needed for adding the timetable
}