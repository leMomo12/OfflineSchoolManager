package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.google.common.truth.Truth
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.data.repository.GradeRepositoryImplTest
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class DeleteSubjectUseCaseTest {

    private lateinit var gradeRepositoryImplTest: GradeRepositoryImplTest
    private lateinit var deleteSubjectUseCase: DeleteSubjectUseCase


    @Before
    fun setUp() {
        gradeRepositoryImplTest = GradeRepositoryImplTest()
        deleteSubjectUseCase = DeleteSubjectUseCase(gradeRepositoryImplTest)
    }

    @Test
    fun deleteSubject(): Unit = runBlocking {
        val grade = Grade(1,1,"hello", 2.53, true, 423)
        val grade2 = Grade(2,2,"english", 2.33, false, 423)
        val grade3 = Grade(3,2,"english", 2.33, false, 423)

        val subject  = Subject(1, "German", 4234, "hello", 50.0 ,50.0, 3.25)
        val subject2  = Subject(2, "English", 4234, "hello", 50.0 ,50.0, 3.25)

        gradeRepositoryImplTest.addGrade(grade)
        gradeRepositoryImplTest.addGrade(grade2)
        gradeRepositoryImplTest.addGrade(grade3)

        gradeRepositoryImplTest.addSubject(subject)
        gradeRepositoryImplTest.addSubject(subject2)

        val job = launch {
            deleteSubjectUseCase.invoke(2).collect() {
                cancel()
            }
        }
        job.join()
        job.cancel()

        gradeRepositoryImplTest.subjectList.forEach {
            println("SubjectList After: $it")
        }

        gradeRepositoryImplTest.gradeList.forEach {
            println("GradeList After: $it")
        }

        Truth.assertThat(gradeRepositoryImplTest.subjectList).contains(subject)
        Truth.assertThat(gradeRepositoryImplTest.subjectList).doesNotContain(subject2)

        Truth.assertThat(gradeRepositoryImplTest.gradeList).doesNotContain(grade2)
        Truth.assertThat(gradeRepositoryImplTest.gradeList).doesNotContain(grade3)
        Truth.assertThat(gradeRepositoryImplTest.gradeList).contains(grade)
    }

}