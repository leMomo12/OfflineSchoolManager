package com.mnowo.offlineschoolmanager.core.feature_core.domain.models

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import java.util.*
import javax.inject.Inject

class TimestampConverter @Inject constructor() {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}