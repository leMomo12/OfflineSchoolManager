package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateAverageUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    operator fun invoke(newAverage: Double, subjectId: Int) : Flow<Boolean> = flow<Boolean> {
        repository.updateAverage(newAverage = newAverage, subjectId = subjectId)
        emit(true)
    }.flowOn(Dispatchers.IO)
}