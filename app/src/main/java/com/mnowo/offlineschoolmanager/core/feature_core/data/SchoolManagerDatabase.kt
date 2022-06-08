package com.mnowo.offlineschoolmanager.core.feature_core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.Converters
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectDao
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_todo.data.local.ToDoDao
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo

@Database(entities = [Subject::class, Grade::class, ToDo::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SchoolManagerDatabase : RoomDatabase() {
    abstract fun gradeDao() : GradeDao
    abstract fun subjectDao() : SubjectDao
    abstract fun toDoDao() : ToDoDao
}