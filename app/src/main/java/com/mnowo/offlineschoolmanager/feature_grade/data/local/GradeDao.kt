package com.mnowo.offlineschoolmanager.feature_grade.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGrade(grade: Grade)

    @Query("SELECT * FROM ${Constants.SUBJECT_TABLE}")
    fun getAllSubjects() : Flow<List<Subject>>

    @Query("SELECT * FROM ${Constants.GRADE_TABLE} WHERE subjectId = :subjectId")
    fun getAllGrades(subjectId: Int) : Flow<List<Grade>>

}