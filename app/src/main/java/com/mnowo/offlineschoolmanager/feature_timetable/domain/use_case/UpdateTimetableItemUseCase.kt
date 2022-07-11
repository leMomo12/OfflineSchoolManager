package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.TimetableResult
import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.util.ValidateTimetable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateTimetableItemUseCase @Inject constructor(
    private val repository: TimetableRepository
) {

    operator fun invoke(
        timetable: Timetable,
        timetableList: List<Timetable>
    ): Flow<Resource<TimetableResult>> =
        flow<Resource<TimetableResult>> {
            when (ValidateTimetable.validateTimetable(
                timetable = timetable,
                timetableList = timetableList
            )) {
                is TimetableResult.AlreadyTaken -> {
                    emit(
                        Resource.Error(message = "", data = TimetableResult.AlreadyTaken)
                    )
                }
                is TimetableResult.EmptyDay -> {
                    emit(
                        Resource.Error(message = "", data = TimetableResult.EmptyDay)
                    )
                }
                is TimetableResult.Success -> {
                    repository.updateTimetableItem(timetable = timetable)
                    emit(Resource.Success(data = TimetableResult.Success))
                }
            }
        }.flowOn(Dispatchers.IO)
}