package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.repository.SubjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class GetAllSubjectsUseCase @Inject constructor(
    private val repository: SubjectRepository
) {

    operator fun invoke(): Flow<Resource<List<Subject>>> = flow {

        try {
            val data = repository.getAllSubjects().collect() {
                emit(Resource.Loading<List<Subject>>(data = it))
                emit(Resource.Success<List<Subject>>(data = it))
            }
        } catch (e: Exception) {
            emit(
                Resource.Error<List<Subject>>(
                    message = (e.localizedMessage ?: R.string.unexpectedError).toString()
                )
            )
        }

    }.flowOn(Dispatchers.IO)
}