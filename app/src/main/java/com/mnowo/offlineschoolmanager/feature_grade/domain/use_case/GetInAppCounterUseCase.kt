package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import javax.inject.Inject

class GetInAppCounterUseCase @Inject constructor(
    private val repository: GradeRepository
) {
    suspend operator fun invoke() : Int = repository.getInAppCounter()
}