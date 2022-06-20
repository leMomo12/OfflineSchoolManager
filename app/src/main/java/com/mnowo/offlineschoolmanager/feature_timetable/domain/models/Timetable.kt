package com.mnowo.offlineschoolmanager.feature_timetable.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.DaysConverter
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TimestampConverter
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants

@Entity(tableName = Constants.TIMETABLE_TABLE)
@TypeConverters(DaysConverter::class)
data class Timetable(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var day: Days,
    var startTime: Long,
    var endTime: Long,
    var subjectId: Int
)
