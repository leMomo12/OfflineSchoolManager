package com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case

import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import javax.inject.Inject

class AddTimetableItemUseCase @Inject constructor(
    private val repository: TimetableRepository
) {

}