package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddGradeUseCase @Inject constructor(
    private val gradeRepository: GradeRepository
) {

    operator fun invoke(grade: Grade) : Flow<Resource<List<Grade>>> = flow {

    }
}