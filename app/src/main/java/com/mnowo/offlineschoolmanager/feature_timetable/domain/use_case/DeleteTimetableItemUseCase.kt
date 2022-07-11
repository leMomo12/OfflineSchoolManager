package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case

import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import javax.inject.Inject

class DeleteTimetableItemUseCase @Inject constructor(
    private val repository: TimetableRepository
) {

    suspend operator fun invoke(timetable: Timetable) =
        repository.deleteTimetableItem(timetable = timetable)
}