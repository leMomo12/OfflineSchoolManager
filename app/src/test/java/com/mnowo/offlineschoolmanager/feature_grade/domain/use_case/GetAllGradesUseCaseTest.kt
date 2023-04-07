package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.google.common.truth.Truth
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetAllGradesUseCaseTest {

    private lateinit var gradeRepositoryImplTest: GradeRepositoryImplTest
    private lateinit var getAllGradesUseCase: GetAllGradesUseCase

    @Before
    fun setUp() {
        gradeRepositoryImplTest = GradeRepositoryImplTest()
        getAllGradesUseCase = GetAllGradesUseCase(repository = gradeRepositoryImplTest)
    }

    @Test
    fun getAllGrades(): Unit = runBlocking {
        val grade = Grade(1, 1, "German", 0.5, true, 4234)
        val grade2 = Grade(2, 1, "German", 0.5, true, 4234)
        val grade3 = Grade(3, 2, "German", 0.5, true, 4234)
        val grade4 = Grade(4, 2, "German", 0.5, true, 4234)

        gradeRepositoryImplTest.addGrade(grade)
        gradeRepositoryImplTest.addGrade(grade2)
        gradeRepositoryImplTest.addGrade(grade3)
        gradeRepositoryImplTest.addGrade(grade4)

        launch {
            getAllGradesUseCase.invoke(subjectId = 1).collect() {
                Truth.assertThat(it.data).contains(grade)
                Truth.assertThat(it.data).contains(grade2)
                Truth.assertThat(it.data).doesNotContain(grade3)
                Truth.assertThat(it.data).doesNotContain(grade4)
                cancel()
            }
        }
    }
}