package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
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
