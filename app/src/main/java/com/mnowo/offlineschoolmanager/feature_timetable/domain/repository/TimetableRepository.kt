package com.mnowo.offlineschoolmanager.feature_timetable.domain.repository

import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {

    fun addTimetableItem(timetable: Timetable)

    fun getAllTimetableItems() : Flow<List<Timetable>>
}