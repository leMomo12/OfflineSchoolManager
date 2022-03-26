package com.mnowo.offlineschoolmanager.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.TestDataClass

@Database(entities = [TestDataClass::class], version = 1, exportSchema = false)
abstract class SchoolManagerDatabase : RoomDatabase() {
    abstract fun gradeDao() : GradeDao
}