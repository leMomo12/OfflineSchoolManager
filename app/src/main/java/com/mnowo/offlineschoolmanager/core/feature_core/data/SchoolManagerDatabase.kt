package com.mnowo.offlineschoolmanager.core.feature_core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectDao
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_todo.data.local.ToDoDao

@Database(entities = [Subject::class, Grade::class], version = 6, exportSchema = false)
abstract class SchoolManagerDatabase : RoomDatabase() {
    abstract fun gradeDao() : GradeDao
    abstract fun subjectDao() : SubjectDao
    abstract fun toDoDao() : ToDoDao
}