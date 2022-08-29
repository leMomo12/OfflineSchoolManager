package com.mnowo.offlineschoolmanager.feature_home.data.local

import androidx.room.Dao
import androidx.room.Query
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable

@Dao
interface HomeDao {

    @Query("SELECT average FROM ${Constants.SUBJECT_TABLE}")
    suspend fun getAverage() : List<Double>

    @Query("SELECT COUNT(*) FROM ${Constants.SUBJECT_TABLE}")
    suspend fun getCountOfSubjects() : Int

    @Query("SELECT * FROM ${Constants.TIMETABLE_TABLE} WHERE day = :day")
    suspend fun getSpecificTimetableDay(day: Int): List<Timetable>

}