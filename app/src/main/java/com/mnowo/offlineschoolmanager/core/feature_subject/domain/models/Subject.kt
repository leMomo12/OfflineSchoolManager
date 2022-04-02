package com.mnowo.offlineschoolmanager.core.feature_subject.domain.models

import android.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.convertTo
import androidx.core.graphics.toColor
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants

@Entity(tableName = Constants.SUBJECT_TABLE)
data class Subject(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var subjectName: String,
    var color: Int,
    var room: String?,
    var oralPercentage: Double,
    var writtenPercentage: Double,
    var average: Double
)
