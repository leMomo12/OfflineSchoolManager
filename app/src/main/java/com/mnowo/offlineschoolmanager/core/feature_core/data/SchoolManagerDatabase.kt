
package com.mnowo.offlineschoolmanager.core.feature_core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.DaysConverter
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TimestampConverter
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectDao
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_exam.data.local.ExamDao
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_home.data.local.HomeDao
import com.mnowo.offlineschoolmanager.feature_timetable.data.local.TimetableDao
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_todo.data.local.ToDoDao
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo

@Database(
    entities = [Subject::class, Grade::class, ToDo::class, Timetable::class, Exam::class],
    version = 30,
    exportSchema = false
)
@TypeConverters(TimestampConverter::class, DaysConverter::class)
abstract class SchoolManagerDatabase : RoomDatabase() {

    abstract fun gradeDao(): GradeDao
    abstract fun subjectDao(): SubjectDao
    abstract fun toDoDao(): ToDoDao
    abstract fun timetableDao(): TimetableDao
    abstract fun homeDao() : HomeDao
    abstract fun examDao() : ExamDao

}