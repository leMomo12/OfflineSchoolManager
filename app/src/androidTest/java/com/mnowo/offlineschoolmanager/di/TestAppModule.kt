package com.mnowo.offlineschoolmanager.di

import android.content.Context
import androidx.room.Room
import com.mnowo.offlineschoolmanager.core.feature_core.data.SchoolManagerDatabase
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectDao
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectRepositoryImpl
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.repository.SubjectRepository
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.data.repository.GradeRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    @Named("test_db")
    fun provideInMemoryDatabase(@ApplicationContext context: Context): SchoolManagerDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            SchoolManagerDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }
}