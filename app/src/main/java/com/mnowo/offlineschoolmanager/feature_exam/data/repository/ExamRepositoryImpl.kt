package com.mnowo.offlineschoolmanager.feature_exam.data.repository

import com.mnowo.offlineschoolmanager.feature_exam.data.local.ExamDao
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_exam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExamRepositoryImpl @Inject constructor(
    private val dao: ExamDao
) : ExamRepository {

    override suspend fun addExam(exam: Exam) {
        return dao.addExam(exam = exam)
    }

    override fun getAllExamItems(): Flow<List<Exam>> {
        return dao.getAllExamItems()
    }

}