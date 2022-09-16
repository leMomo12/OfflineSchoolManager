package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import android.util.Log.d
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.RoundOffDecimals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject

class UpdateAverageUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    operator fun invoke(subjectId: Int): Flow<Boolean> = flow<Boolean> {

        val getSpecificSubject = repository.getSpecificSubject(subjectId = subjectId)

        val oralSum = repository.sumOfOralGrade(subjectId = subjectId)
        val oralSize = repository.countOfOralGrade(subjectId = subjectId)

        val writtenSum = repository.sumOfWrittenGrade(subjectId = subjectId)
        val writtenSize = repository.countOfWrittenGrade(subjectId = subjectId)

        val oralAverage: Double = oralSum / oralSize
        val writtenAverage: Double = writtenSum / writtenSize


        var newAverage: Double = 0.0

        newAverage = when {
            oralAverage.isNaN() && !writtenAverage.isNaN() -> {
                writtenAverage
            }
            writtenAverage.isNaN() && !oralAverage.isNaN() -> {
                oralAverage
            }
            else -> {
                (oralAverage * getSpecificSubject.oralPercentage +
                        writtenAverage * getSpecificSubject.writtenPercentage) / 100
            }
        }

        val newAverageRounded = RoundOffDecimals.roundOffDoubleDecimals(grade = newAverage)
        repository.updateAverage(newAverage = newAverageRounded, subjectId = subjectId)
        emit(true)

    }.flowOn(Dispatchers.IO).catch { }
}