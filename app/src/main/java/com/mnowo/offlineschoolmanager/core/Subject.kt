package com.mnowo.offlineschoolmanager.core

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Constants.SUBJECT_TABLE)
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val subjectName: String,
    val color: Color,
    val room: String?
)
