package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetAllGradesUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    operator fun invoke(subjectId: Int): Flow<Resource<List<Grade>>> = flow {

        try {
            repository.getAllGrades(subjectId = subjectId).collect() {
                emit(Resource.Loading<List<Grade>>(data = it))
                emit(Resource.Success<List<Grade>>(data = it))
            }
        } catch (e: Exception) {
            emit(
                Resource.Error<List<Grade>>(
                    message = (e.localizedMessage ?: R.string.unexpectedError).toString()
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}