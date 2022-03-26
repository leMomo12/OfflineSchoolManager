package com.mnowo.offlineschoolmanager.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mnowo.offlineschoolmanager.core.Constants
import com.mnowo.offlineschoolmanager.core.SchoolManagerDatabase
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.data.repository.GradeRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_grade.domain.GradeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
        gradeDao: GradeDao
    ): GradeRepository {
        return GradeRepositoryImpl(
            gradeDao = gradeDao
        )
    }

    @Provides
    @Singleton
    fun provideGradeDao(db: SchoolManagerDatabase) = db.gradeDao()
}