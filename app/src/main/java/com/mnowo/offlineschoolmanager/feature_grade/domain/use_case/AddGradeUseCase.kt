package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import android.content.Context
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.AddGradeResult
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.CalculateGradeColor
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

    operator fun invoke(grade: Grade): Flow<Resource<AddGradeResult>> = flow {
        try {
            emit(Resource.Loading<AddGradeResult>())
            if (grade.description.trim().isBlank()) {
                emit(
                    Resource.Error<AddGradeResult>(
                        message = context.getString(R.string.emptyClassTestDescription),
                        data = AddGradeResult.EmptyDescription
                    )
                )
                return@flow
            }
            val roundedGrade = RoundOffDecimals.roundOffDoubleDecimals(grade = grade.grade)
            val color = CalculateGradeColor.calculateGradeColor(grade = grade.grade)

            if (color == -1) {
                emit(
                    Resource.Error<AddGradeResult>(
                        message = context.getString(R.string.unexpectedError),
                        data = AddGradeResult.GradeOutOffRange
                    )
                )
                return@flow
            }
            gradeRepository.addGrade(grade = grade.copy(gradeColor = color, grade = roundedGrade))
            emit(Resource.Success<AddGradeResult>(AddGradeResult.Success))
        } catch (e: Exception) {
            emit(
                Resource.Error<AddGradeResult>(
                    message = e.localizedMessage ?: context.getString(R.string.unexpectedError),
                    data = AddGradeResult.Exception
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}