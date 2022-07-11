package com.mnowo.offlineschoolmanager.feature_timetable.data.local

import androidx.room.*
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTimetableItem(timetable: Timetable)

    @Query("SELECT * FROM ${Constants.TIMETABLE_TABLE}")
    fun getAllTimetableItems() : Flow<List<Timetable>>

    @Update
    suspend fun updateTimetableItem(timetable: Timetable)

    @Delete
    suspend fun deleteTimetableItem(timetable: Timetable)

    @Query("Delete FROM ${Constants.TIMETABLE_TABLE}")
    suspend fun deleteEntireTimetable()

}