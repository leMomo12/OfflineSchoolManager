package com.mnowo.offlineschoolmanager.feature_exam.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.ExamResult
import com.mnowo.offlineschoolmanager.feature_exam.domain.repository.ExamRepository
import com.mnowo.offlineschoolmanager.feature_exam.domain.use_case.util.ExamValidation
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EditExamItemUseCase @Inject constructor(
    private val repository: ExamRepository
) {

    operator fun invoke(exam: Exam): Flow<Resource<ExamResult>> = flow<Resource<ExamResult>> {
        when (ExamValidation.validateExam(exam = exam)) {
            ToDoResult.EmptyDescription -> {
                emit(Resource.Error(message = "", data = ExamResult.EmptyDescription))
            }
            ToDoResult.EmptyTitle -> {
                emit(Resource.Error(message = "", data = ExamResult.EmptyTitle))
            }
            ToDoResult.SubjectNotPicked -> {
                emit(Resource.Error(message = "", data = ExamResult.SubjectNotPicked))
            }
            ToDoResult.Success -> {
                repository.updateExamItem(exam = exam)
                emit(Resource.Success(data = ExamResult.Success))
            }
        }
    }.flowOn(Dispatchers.IO)
}