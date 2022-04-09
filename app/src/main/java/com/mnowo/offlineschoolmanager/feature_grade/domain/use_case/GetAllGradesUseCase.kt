package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.repository.SubjectRepository
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllGradesUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    operator fun invoke(subjectId: Int): Flow<Resource<List<Grade>>> = flow {
        val data = repository.getAllGrades(subjectId = subjectId).first()

        try {
            emit(Resource.Loading<List<Grade>>(data = data))
            emit(Resource.Success<List<Grade>>(data = data))
        } catch (e: Exception) {
            emit(
                Resource.Error<List<Grade>>(
                    message = (e.localizedMessage ?: R.string.unexpectedError).toString()
                )
            )
        }
    }
}