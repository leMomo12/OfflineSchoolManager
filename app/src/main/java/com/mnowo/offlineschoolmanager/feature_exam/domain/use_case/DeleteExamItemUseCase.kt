package com.mnowo.offlineschoolmanager.feature_exam.domain.use_case

import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_exam.domain.repository.ExamRepository
import javax.inject.Inject

class DeleteExamItemUseCase @Inject constructor(
    private val repository: ExamRepository
) {

    suspend operator fun invoke(id: Int) = repository.deleteExamItem(id = id)
}