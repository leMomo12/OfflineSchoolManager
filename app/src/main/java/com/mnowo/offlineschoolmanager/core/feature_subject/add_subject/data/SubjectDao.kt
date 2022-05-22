package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data

import androidx.room.*
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubject(subject: Subject)

    @Query("SELECT * FROM ${Constants.SUBJECT_TABLE}")
    fun getAllSubjects() : Flow<List<Subject>>

    @Query("DELETE FROM ${Constants.SUBJECT_TABLE} WHERE id = :id")
    suspend fun deleteSubject(id: Int)

    @Update
    suspend fun updateSubject(subject: Subject)
}