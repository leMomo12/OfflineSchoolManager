package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetAllSubjectsUseCase @Inject constructor(
    private val repository: SubjectRepository
) {

    operator fun invoke(): Flow<Resource<List<Subject>>> = flow {
        val data = repository.getAllSubjects().first()

        try {
            emit(Resource.Loading<List<Subject>>(data = data))
            emit(Resource.Success<List<Subject>>(data = data))
        } catch (e: Exception) {
            emit(
                Resource.Error<List<Subject>>(
                    message = (e.localizedMessage ?: R.string.unexpectedError).toString()
                )
            )
        }

    }
}