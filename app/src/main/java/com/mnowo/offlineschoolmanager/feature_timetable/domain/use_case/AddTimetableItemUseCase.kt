package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.TimetableResult
import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.util.ValidateTimetable
import com.mnowo.offlineschoolmanager.feature_timetable.presentation.TimetableEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTimetableItemUseCase @Inject constructor(
    private val repository: TimetableRepository
) {

    operator fun invoke(timetable: Timetable) : Flow<Resource<TimetableResult>> = flow {
        when (val validation = ValidateTimetable.validateTimetable(timetable = timetable)) {
            is TimetableResult.EmptyDay -> {
                emit(Resource.Error<TimetableResult>(message = "", data = TimetableResult.EmptyDay))
            }
            is TimetableResult.Success -> {
                repository.addTimetableItem(timetable = timetable)
                emit(Resource.Success<TimetableResult>(data = TimetableResult.Success))
            }
        }
    }
}