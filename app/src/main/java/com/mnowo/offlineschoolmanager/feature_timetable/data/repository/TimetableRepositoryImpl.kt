package com.mnowo.offlineschoolmanager.feature_timetable.data.repository

import com.mnowo.offlineschoolmanager.feature_timetable.data.local.TimetableDao
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(
    private val timetableDao: TimetableDao
) : TimetableRepository {

    override fun addTimetableItem(timetable: Timetable) {
        return timetableDao.addTimetableItem(timetable = timetable)
    }

    override fun getAllTimetableItems(): Flow<List<Timetable>> {
        return timetableDao.getAllTimetableItems()
    }


}