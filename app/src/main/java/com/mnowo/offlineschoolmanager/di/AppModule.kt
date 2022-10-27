package com.mnowo.offlineschoolmanager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_core.data.SchoolManagerDatabase
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.ReviewService
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectDao
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectRepositoryImpl
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.repository.SubjectRepository
import com.mnowo.offlineschoolmanager.feature_exam.data.local.ExamDao
import com.mnowo.offlineschoolmanager.feature_exam.data.repository.ExamRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_exam.domain.repository.ExamRepository
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.data.repository.GradeRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import com.mnowo.offlineschoolmanager.feature_home.data.local.HomeDao
import com.mnowo.offlineschoolmanager.feature_home.data.repository.HomeRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_home.domain.repository.HomeRepository
import com.mnowo.offlineschoolmanager.feature_timetable.data.local.TimetableDao
import com.mnowo.offlineschoolmanager.feature_timetable.data.repository.TimetableRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_timetable.domain.repository.TimetableRepository
import com.mnowo.offlineschoolmanager.feature_todo.data.local.ToDoDao
import com.mnowo.offlineschoolmanager.feature_todo.data.repository.ToDoRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_todo.domain.repository.ToDoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("com.mnowo.offlineschoolmanager.user_in_app_counter")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSchoolManagerDatabase(@ApplicationContext context: Context): SchoolManagerDatabase {
        return Room.databaseBuilder(
            context,
            SchoolManagerDatabase::class.java,
            Constants.DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGradeRepository(
        gradeDao: GradeDao,
        dataStoreManager: DataStore<Preferences>
    ): GradeRepository {
        return GradeRepositoryImpl(
            gradeDao = gradeDao,
            dataStorePreferences = dataStoreManager
        )
    }

    @Provides
    @Singleton
    fun provideGradeDao(db: SchoolManagerDatabase) = db.gradeDao()

    @Provides
    @Singleton
    fun provideSubjectDao(db: SchoolManagerDatabase) = db.subjectDao()

    @Provides
    @Singleton
    fun provideSubjectRepository(
        subjectDao: SubjectDao
    ): SubjectRepository {
        return SubjectRepositoryImpl(
            dao = subjectDao
        )
    }

    @Provides
    @Singleton
    fun provideToDoRepository(
        toDoDao: ToDoDao
    ): ToDoRepository {
        return ToDoRepositoryImpl(
            toDoDao = toDoDao
        )
    }

    @Provides
    @Singleton
    fun provideToDoDao(db: SchoolManagerDatabase) = db.toDoDao()


    @Provides
    @Singleton
    fun provideTimetableRepository(
        timetableDao: TimetableDao
    ): TimetableRepository {
        return TimetableRepositoryImpl(timetableDao = timetableDao)
    }

    @Provides
    @Singleton
    fun provideTimetableDao(db: SchoolManagerDatabase) = db.timetableDao()

    @Provides
    @Singleton
    fun provideHomeRepository(
        dao: HomeDao
    ): HomeRepository {
        return HomeRepositoryImpl(
            dao = dao
        )
    }

    @Provides
    @Singleton
    fun provideHomeDao(db: SchoolManagerDatabase) = db.homeDao()

    @Provides
    @Singleton
    fun provideExamDao(db: SchoolManagerDatabase) = db.examDao()

    @Provides
    @Singleton
    fun provideExamRepository(
        dao: ExamDao
    ): ExamRepository {
        return ExamRepositoryImpl(dao = dao)
    }

    @Provides
    @Singleton
    fun dataStoreManager(@ApplicationContext appContext: Context): DataStore<Preferences> =
        appContext.dataStore

    @Provides
    @Singleton
    fun provideReviewService(): ReviewService = ReviewService
}