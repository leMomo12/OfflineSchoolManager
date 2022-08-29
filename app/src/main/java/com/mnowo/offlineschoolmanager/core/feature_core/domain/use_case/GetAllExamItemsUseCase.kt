package com.mnowo.offlineschoolmanager.core.feature_core.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_exam.domain.repository.ExamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllExamItemsUseCase @Inject constructor(
    private val repository: ExamRepository
) {

    operator fun invoke() : Flow<Resource<List<Exam>>> = flow<Resource<List<Exam>>> {
        repository.getAllExamItems().collect() {
            emit(Resource.Loading<List<Exam>>(data = it))
        }
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(Resource.Error<List<Exam>>(message = it.message ?: "Error occurred"))
        }
}