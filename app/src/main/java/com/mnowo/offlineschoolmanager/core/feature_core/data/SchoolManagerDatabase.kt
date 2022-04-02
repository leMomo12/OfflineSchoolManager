package com.mnowo.offlineschoolmanager.core.feature_core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mnowo.offlineschoolmanager.core.feature_subject.data.SubjectDao
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.TestDataClass

@Database(entities = [Subject::class], version = 3, exportSchema = false)
abstract class SchoolManagerDatabase : RoomDatabase() {
    abstract fun gradeDao() : GradeDao
    abstract fun subjectDao() : SubjectDao
}