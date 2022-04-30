package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import android.content.Context
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.GradeResult
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.CalculateGradeColor
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.GradeValidation
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.RoundOffDecimals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddGradeUseCase @Inject constructor(
    private val gradeRepository: GradeRepository,
    private val context: Context
) {

    operator fun invoke(grade: Grade): Flow<Resource<GradeResult>> = flow {

        when (val validationResult = GradeValidation.validateGrade(grade = grade)) {
            is GradeResult.EmptyDescription -> {
                emit(
                    Resource.Error<GradeResult>(
                        message = context.getString(R.string.emptyClassTestDescription),
                        data = GradeResult.EmptyDescription
                    )
                )
                return@flow
            }
            is GradeResult.GradeOutOffRange -> {
                emit(
                    Resource.Error<GradeResult>(
                        message = context.getString(R.string.gradeOutOfRange),
                        data = GradeResult.GradeOutOffRange
                    )
                )
                return@flow
            }
            is GradeResult.Exception -> {
                emit(
                    Resource.Error<GradeResult>(
                        message = validationResult.message
                            ?: context.getString(R.string.unexpectedError),
                        data = GradeResult.Exception()
                    )
                )
                return@flow
            }
            is GradeResult.Success -> {
                gradeRepository.addGrade(
                    grade = grade.copy(
                        gradeColor = validationResult.gradeColor!!,
                        grade = validationResult.roundedGrade!!
                    )
                )
                emit(Resource.Success<GradeResult>(GradeResult.Success()))
                return@flow
            }
        }
    }.flowOn(Dispatchers.IO)
}