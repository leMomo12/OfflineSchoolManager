package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case

import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import javax.inject.Inject

class DeleteEntireTimetableUseCase @Inject constructor(
    private val repository: TimetableRepository
) {

    suspend operator fun invoke() = repository.deleteEntireTimetable()
}