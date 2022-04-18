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
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM ${Constants.GRADE_TABLE} WHERE subjectId = :subjectId")
    fun getAllGrades(subjectId: Int): Flow<List<Grade>>

    @Query("UPDATE ${Constants.SUBJECT_TABLE} SET average = :newAverage WHERE id = :subjectId")
    fun updateAverage(newAverage: Double, subjectId: Int)

    @Query("SELECT * FROM ${Constants.SUBJECT_TABLE} WHERE id = :subjectId")
    fun getSpecificSubject(subjectId: Int): Subject

    @Query("SELECT SUM(grade) FROM ${Constants.GRADE_TABLE} WHERE subjectId = :subjectId AND isWritten = 1")
    fun sumOfWrittenGrade(subjectId: Int): Double

    @Query("SELECT COUNT(*) FROM ${Constants.GRADE_TABLE} WHERE subjectId = :subjectId AND isWritten = 1")
    fun countOfWrittenGrade(subjectId: Int): Int

    @Query("SELECT SUM(grade) FROM ${Constants.GRADE_TABLE} WHERE subjectId = :subjectId AND isWritten = 0")
    fun sumOfOralGrade(subjectId: Int): Double

    @Query("SELECT COUNT(*) FROM ${Constants.GRADE_TABLE} WHERE subjectId = :subjectId AND isWritten = 0")
    fun countOfOralGrade(subjectId: Int): Int

}