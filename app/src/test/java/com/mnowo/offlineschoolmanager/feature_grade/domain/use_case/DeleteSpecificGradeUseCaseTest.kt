package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.google.common.truth.Truth
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeleteSpecificGradeUseCaseTest {

    private lateinit var addGradeUseCase: AddGradeUseCase
    private lateinit var repositoryImpl: GradeRepositoryImplTest

    @Before
    fun setUp() {
        repositoryImpl = GradeRepositoryImplTest()
        addGradeUseCase = AddGradeUseCase(gradeRepository = repositoryImpl)
    }

    @Test
    fun deleteSpecificGrade(): Unit = runBlocking {
        val grade = Grade(1, 1, "German", 3.25, true, 234)
        val grade2 = Grade(2,2,"German",4.5,true,645)

        repositoryImpl.addGrade(grade)
        repositoryImpl.addGrade(grade2)

        repositoryImpl.deleteSpecificGrade(1)


        Truth.assertThat(repositoryImpl.gradeList).contains(grade2)
        Truth.assertThat(repositoryImpl.gradeList).doesNotContain(grade)
    }
}