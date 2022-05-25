package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.google.common.truth.Truth
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.data.repository.GradeRepositoryImplTest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetSpecificSubjectUseCaseTest {

    private lateinit var repositoryImplTest: GradeRepositoryImplTest
    private lateinit var getSpecificSubjectUseCase: GetSpecificSubjectUseCase

    @Before
    fun setUp() {
        repositoryImplTest = GradeRepositoryImplTest()
        getSpecificSubjectUseCase = GetSpecificSubjectUseCase(repositoryImplTest)
    }

    @Test
    fun getSpecificSubject(): Unit = runBlocking {
        val subject = Subject(1, "German", 234, "123", 50.0, 50.0, 2.5)
        val subject2 = Subject(2, "English", 234, "123", 50.0, 50.0, 2.5)

        repositoryImplTest.addSubject(subject)
        repositoryImplTest.addSubject(subject2)

        launch {
            getSpecificSubjectUseCase.invoke(subjectId = 1).collect() {
                Truth.assertThat(it).isEqualTo(subject)
                cancel()
            }
        }
    }
}