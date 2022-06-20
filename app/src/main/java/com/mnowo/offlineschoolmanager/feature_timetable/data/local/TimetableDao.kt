package com.mnowo.offlineschoolmanager.feature_timetable.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTimetableItem(timetable: Timetable)

    @Query("SELECT * FROM ${Constants.TIMETABLE_TABLE}")
    fun getAllTimetableItems() : Flow<List<Timetable>>
}