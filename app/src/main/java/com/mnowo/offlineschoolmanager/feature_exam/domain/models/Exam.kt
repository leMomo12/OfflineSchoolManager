package com.mnowo.offlineschoolmanager.feature_exam.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import java.util.*

@Entity(tableName = Constants.EXAM_TABLE)
data class Exam(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var description: String,
    var subjectId: Int,
    var date: Long
)
