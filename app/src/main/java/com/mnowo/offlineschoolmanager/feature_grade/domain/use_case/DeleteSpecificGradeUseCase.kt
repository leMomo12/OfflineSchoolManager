package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import android.content.Context
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteSpecificGradeUseCase @Inject constructor(
    private val repository: GradeRepository,
    private val context: Context
) {
    operator fun invoke(gradeId: Int): Flow<Resource<Boolean>> = flow<Resource<Boolean>> {
        when (repository.deleteSpecificGrade(gradeId = gradeId)) {
            0 -> {
                emit(Resource.Error<Boolean>(message = context.getString(R.string.unexpectedError)))
            }
            1 -> {
                emit(Resource.Success<Boolean>(data = true))
            }
        }
    }.flowOn(Dispatchers.IO)
}