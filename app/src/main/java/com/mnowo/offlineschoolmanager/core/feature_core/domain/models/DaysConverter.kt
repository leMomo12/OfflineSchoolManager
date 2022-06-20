package com.mnowo.offlineschoolmanager.core.feature_core.domain.models

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days
import javax.inject.Inject

class DaysConverter @Inject constructor() {

    @TypeConverter
    fun fromDays(value: Days) : Int {
        return value.ordinal
    }

    @TypeConverter
    fun toDays(value: Int) = enumValues<Days>()[value]
}