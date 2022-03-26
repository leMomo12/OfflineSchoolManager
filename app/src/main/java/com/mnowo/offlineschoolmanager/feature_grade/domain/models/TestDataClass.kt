package com.mnowo.offlineschoolmanager.feature_grade.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestDataClass(
    @PrimaryKey(autoGenerate = false)
    val abc: String
)
