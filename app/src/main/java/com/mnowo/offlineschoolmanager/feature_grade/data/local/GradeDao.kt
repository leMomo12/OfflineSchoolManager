package com.mnowo.offlineschoolmanager.feature_grade.data.local

import androidx.room.Dao
import androidx.room.Query
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {

    @Query("SELECT * FROM ${Constants.SUBJECT_TABLE}")
    fun getAllSubjects() : Flow<List<Subject>>
}