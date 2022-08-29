package com.mnowo.offlineschoolmanager.feature_exam.domain.repository

import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import kotlinx.coroutines.flow.Flow


interface ExamRepository {

    suspend fun addExam(exam: Exam)

    fun getAllExamItems() : Flow<List<Exam>>

    suspend fun deleteExamItem(id: Int)

    suspend fun updateExamItem(exam: Exam)

}