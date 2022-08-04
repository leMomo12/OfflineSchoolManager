package com.mnowo.offlineschoolmanager.feature_grade.domain.repository

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {

    fun addGrade(grade: Grade)

    fun getAllSubjects() : Flow<List<Subject>>

    fun getAllGrades(subjectId: Int) : Flow<List<Grade>>

    fun updateAverage(newAverage: Double, subjectId: Int)

    fun getSpecificSubject(subjectId: Int) : Subject

    fun sumOfWrittenGrade(subjectId: Int): Double

    fun countOfWrittenGrade(subjectId: Int): Int

    fun sumOfOralGrade(subjectId: Int): Double

    fun countOfOralGrade(subjectId: Int): Int

    suspend fun deleteSpecificGrade(gradeId: Int): Int

    suspend fun updateGrade(grade: Grade)

    suspend fun deleteSubject(subjectId: Int)

    suspend fun deleteAllSubjectSpecificGrades(subjectId: Int)

    suspend fun deleteAllSubjectsFromTimetable(subjectId: Int)

    suspend fun deleteAllSubjectsFromToDo(subjectId: Int)

    suspend fun deleteAllSubjectsFromExam(subjectId: Int)

}