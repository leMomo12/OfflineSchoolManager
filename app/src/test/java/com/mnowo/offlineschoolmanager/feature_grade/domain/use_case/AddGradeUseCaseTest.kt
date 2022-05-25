package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.google.common.truth.Truth
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.data.SubjectRepositoryImpl
import com.mnowo.offlineschoolmanager.feature_grade.data.repository.GradeRepositoryImplTest
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.GradeResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AddGradeUseCaseTest {

    private lateinit var addGradeUseCase: AddGradeUseCase
    private lateinit var repositoryImpl: GradeRepositoryImplTest

    @Before
    fun setUp() {
        repositoryImpl = GradeRepositoryImplTest()
        addGradeUseCase = AddGradeUseCase(gradeRepository = repositoryImpl)
    }

    @Test
    fun emptyDescription() = runBlocking {
        val grade = Grade(1, 1, "", 2.0, true, 4234)
        val job = launch(Dispatchers.IO) {
            val res = addGradeUseCase.invoke(grade = grade).first().data
            Truth.assertThat(res)
                .isEqualTo(
                    GradeResult.EmptyDescription
                )

        }
        job.join()
        job.cancel()
    }

    @Test
    fun gradeOutOfRange() = runBlocking {
        val grade = Grade(1, 1, "German", 0.5, true, 4234)
        val job = launch(Dispatchers.IO) {
            val res = addGradeUseCase.invoke(grade = grade).first().data
            Truth.assertThat(res)
                .isEqualTo(
                    GradeResult.GradeOutOffRange
                )

        }
        job.join()
        job.cancel()
    }

    @Test
    fun success() = runBlocking {
        val grade = Grade(1, 1, "German", 2.0, true, 4234)
        val job = launch(Dispatchers.IO) {
            val res = addGradeUseCase.invoke(grade = grade).first().data
            Truth.assertThat(res)
                .isEqualTo(
                    GradeResult.Success()
                )

        }
        job.join()
        job.cancel()
    }
}