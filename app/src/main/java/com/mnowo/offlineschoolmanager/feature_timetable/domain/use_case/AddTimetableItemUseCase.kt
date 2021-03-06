package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.TimetableResult
import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.util.ValidateTimetable
import com.mnowo.offlineschoolmanager.feature_timetable.presentation.TimetableEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddTimetableItemUseCase @Inject constructor(
    private val repository: TimetableRepository
) {

    operator fun invoke(
        timetable: Timetable,
        timetableList: List<Timetable>
    ): Flow<Resource<TimetableResult>> = flow {
        when (ValidateTimetable.validateTimetable(
            timetable = timetable,
            timetableList = timetableList
        )) {
            is TimetableResult.EmptyDay -> {
                emit(Resource.Error<TimetableResult>(message = "", data = TimetableResult.EmptyDay))
            }
            is TimetableResult.AlreadyTaken -> {
                emit(
                    Resource.Error<TimetableResult>(
                        message = "",
                        data = TimetableResult.AlreadyTaken
                    )
                )
            }
            is TimetableResult.Success -> {
                repository.addTimetableItem(timetable = timetable)
                emit(Resource.Success<TimetableResult>(data = TimetableResult.Success))
            }
        }
    }.flowOn(Dispatchers.IO)
}