package com.mnowo.offlineschoolmanager.feature_todo.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import java.util.*

@Entity(tableName = Constants.TODO_TABLE)
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var description: String,
    var until: Date,
    var isChecked: Boolean,
    var subjectId: Int
)
