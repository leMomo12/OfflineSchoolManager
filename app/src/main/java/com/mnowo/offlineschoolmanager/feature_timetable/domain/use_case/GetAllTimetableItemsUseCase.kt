package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllTimetableItemsUseCase @Inject constructor(
    private val repository: TimetableRepository
) {

    operator fun invoke(): Flow<Resource<List<Timetable>>> = flow<Resource<List<Timetable>>> {
        repository.getAllTimetableItems().collect() {
            emit(Resource.Loading(data = it))
            emit(Resource.Success(data = it))
        }
    }.flowOn(Dispatchers.IO)
}