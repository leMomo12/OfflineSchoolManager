package com.mnowo.offlineschoolmanager.feature_grade.domain.repository

import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {

    fun addGrade(grade: Grade)

    fun getAllSubjects() : Flow<List<Subject>>

    fun getAllGrades(subjectId: Int) : Flow<List<Grade>>

    fun updateAverage(newAverage: Double, subjectId: Int)

    fun getSpecificSubject(subjectId: Int) : Subject
}