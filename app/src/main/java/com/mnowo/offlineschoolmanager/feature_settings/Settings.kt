package com.mnowo.offlineschoolmanager.feature_settings

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants

@Entity(tableName = Constants.SETTINGS_TABLE)
data class Settings(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val isNormalGrade: Boolean
)
