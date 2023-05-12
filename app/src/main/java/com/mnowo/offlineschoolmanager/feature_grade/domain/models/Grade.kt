package com.mnowo.offlineschoolmanager.feature_grade.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.GradeSystem
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants

@Entity(tableName = Constants.GRADE_TABLE)
data class Grade(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var subjectId: Int,
    var description: String,
    var grade: Double,
    var isWritten: Boolean,
    var gradeColor: Int = 0
)
