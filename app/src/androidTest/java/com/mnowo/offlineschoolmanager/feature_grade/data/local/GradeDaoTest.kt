package com.mnowo.offlineschoolmanager.feature_grade.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.mnowo.offlineschoolmanager.core.feature_core.data.SchoolManagerDatabase
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectDao
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.di.AppModule
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@RunWith(AndroidJUnit4::class)
@SmallTest
class GradeDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: SchoolManagerDatabase
    lateinit var gradeDao: GradeDao
    lateinit var subjectDao: SubjectDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SchoolManagerDatabase::class.java
        ).allowMainThreadQueries().build()
        gradeDao = database.gradeDao()
        subjectDao = database.subjectDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addGrade() = runBlocking {
        val grade = Grade(
            1,
            1,
            "Test",
            2.0,
            true
        )
        gradeDao.addGrade(grade)
        val job = launch(Dispatchers.IO) {
            val res = gradeDao.getAllGrades(1).first()
            Truth.assertThat(res).contains(grade)
        }
        job.join()
        job.cancel()
    }

    @Test
    fun updateAverage() = runBlocking {
        val subject = Subject(
            1,
            "German",
            0,
            "31",
            50.0,
            50.0,
            2.25
        )
        subjectDao.addSubject(subject)
        gradeDao.updateAverage(3.0, 1)
        val res = gradeDao.getSpecificSubject(1).average
        Truth.assertThat(res).isEqualTo(3.0)
    }

    @Test
    fun getSpecificSubject() = runBlocking {
        val subject = Subject(
            1,
            "German",
            0,
            "31",
            50.0,
            50.0,
            2.25
        )
        subjectDao.addSubject(subject)
        val res = gradeDao.getSpecificSubject(1)
        Truth.assertThat(res).isEqualTo(subject)
    }

    @Test
    fun sumOfWrittenGrade() = runBlocking {
        val grade = Grade(1, 1, "Hello", 2.0, true)
        val grade2 = Grade(2, 1, "Hello", 2.0, true)
        val grade3 = Grade(3, 2, "Hello", 2.0, true)
        val grade4 = Grade(4, 2, "Hello", 2.0, false)
        gradeDao.addGrade(grade)
        gradeDao.addGrade(grade2)
        gradeDao.addGrade(grade3)
        gradeDao.addGrade(grade4)
        val res = gradeDao.sumOfWrittenGrade(1)
        Truth.assertThat(res).isEqualTo(4.0)
    }

    @Test
    fun countOfWrittenGrade() = runBlocking {
        val grade = Grade(1, 1, "Hello", 2.0, true)
        val grade2 = Grade(2, 1, "Hello", 2.0, true)
        val grade3 = Grade(3, 2, "Hello", 2.0, true)
        val grade4 = Grade(4, 2, "Hello", 2.0, false)
        gradeDao.addGrade(grade)
        gradeDao.addGrade(grade2)
        gradeDao.addGrade(grade3)
        gradeDao.addGrade(grade4)
        val res = gradeDao.countOfWrittenGrade(1)
        Truth.assertThat(res).isEqualTo(2)
    }

    @Test
    fun sumOfOralGrade() = runBlocking {
        val grade = Grade(1, 1, "Hello", 2.0, false)
        val grade2 = Grade(2, 1, "Hello", 2.0, false)
        val grade3 = Grade(3, 2, "Hello", 2.0, false)
        val grade4 = Grade(4, 2, "Hello", 2.0, true)
        gradeDao.addGrade(grade)
        gradeDao.addGrade(grade2)
        gradeDao.addGrade(grade3)
        gradeDao.addGrade(grade4)
        val res = gradeDao.sumOfOralGrade(1)
        Truth.assertThat(res).isEqualTo(4.0)
    }

    @Test
    fun countOfOralGrade() = runBlocking {
        val grade = Grade(1, 1, "Hello", 2.0, false)
        val grade2 = Grade(2, 1, "Hello", 2.0, false)
        val grade3 = Grade(3, 2, "Hello", 2.0, false)
        val grade4 = Grade(4, 2, "Hello", 2.0, true)
        gradeDao.addGrade(grade)
        gradeDao.addGrade(grade2)
        gradeDao.addGrade(grade3)
        gradeDao.addGrade(grade4)
        val res = gradeDao.countOfOralGrade(1)
        Truth.assertThat(res).isEqualTo(2)
    }

    @Test
    fun deleteSpecificGrade() = runBlocking {
        val grade = Grade(1, 1, "Hello", 2.0, false)
        val grade2 = Grade(2, 1, "Hello", 2.0, false)
        gradeDao.addGrade(grade)
        gradeDao.addGrade(grade2)
        gradeDao.deleteSpecificGrade(1)
        val job = launch(Dispatchers.IO) {
            val res = gradeDao.getAllGrades(1).first()
            Truth.assertThat(res).contains(grade2)
        }
        job.join()
        job.cancel()
    }

    @Test
    fun updateGrade() = runBlocking {
        val grade = Grade(1, 1, "Hello", 2.0, false)
        gradeDao.addGrade(grade)
        val updatedGrade = Grade(1, 1, "New grade", 2.5, true, 234)
        gradeDao.updateGrade(updatedGrade)
        val job = launch(Dispatchers.IO) {
            val res = gradeDao.getAllGrades(1).first()
            Truth.assertThat(res).contains(updatedGrade)
        }
        job.join()
        job.cancel()
    }

    @Test
    fun deleteSubject() = runBlocking {
        val subject = Subject(1,
            "Hello",
            234,
            "234",
            50.0,
            50.0,
            2.0
        )
        subjectDao.addSubject(subject)
        gradeDao.deleteSubject(1)
        val job = launch(Dispatchers.IO) {
            val res = gradeDao.getAllSubjects().first()
            Truth.assertThat(res).isEmpty()
        }
        job.join()
        job.cancel()
    }

    @Test
    fun deleteAllSubjectSpecificGrades() = runBlocking {
        val grade = Grade(1, 1, "Hello", 2.0, false)
        val grade2 = Grade(2, 1, "Hello", 2.0, false)
        val grade3 = Grade(3, 2, "Hello", 2.0, false)
        val grade4 = Grade(4, 2, "Hello", 2.0, true)
        gradeDao.addGrade(grade)
        gradeDao.addGrade(grade2)
        gradeDao.addGrade(grade3)
        gradeDao.addGrade(grade4)
        gradeDao.deleteAllSubjectSpecificGrades(2)
        val job = launch(Dispatchers.IO) {
            val res = gradeDao.getAllSubjects().toList()
            Truth.assertThat(res).hasSize(2)
            Truth.assertThat(res).contains(grade)
            Truth.assertThat(res).contains(grade2)
        }
        job.join()
        job.cancel()
    }
}