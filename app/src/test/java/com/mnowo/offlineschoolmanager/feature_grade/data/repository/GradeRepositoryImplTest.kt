package com.mnowo.offlineschoolmanager.feature_grade.data.repository

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

import org.junit.Test

class GradeRepositoryImplTest : GradeRepository {

    var gradeList = mutableListOf<Grade>()

    var subjectList = mutableListOf<Subject>()

    fun addSubject(subject: Subject) {
        subjectList.add(subject)
        return
    }

    override fun addGrade(grade: Grade) {
        gradeList.add(grade)
        return
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        TODO("Not yet implemented")
    }

    override fun getAllGrades(subjectId: Int): Flow<List<Grade>> {
        return flow<List<Grade>> {
            emit(gradeList.filter { it.subjectId == subjectId })
        }
    }

    override fun updateAverage(newAverage: Double, subjectId: Int) {
        TODO("Not yet implemented")
    }

    override fun getSpecificSubject(subjectId: Int): Subject {
        subjectList.forEach {
            if (it.id == subjectId) {
                return it
            }
        }
        return Subject(
            -1,
            "German",
            4324,
            "423",
            50.0,
            50.0,
            3.25
        )
    }

    override fun sumOfWrittenGrade(subjectId: Int): Double {
        TODO("Not yet implemented")
    }

    override fun countOfWrittenGrade(subjectId: Int): Int {
        TODO("Not yet implemented")
    }

    override fun sumOfOralGrade(subjectId: Int): Double {
        TODO("Not yet implemented")
    }

    override fun countOfOralGrade(subjectId: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpecificGrade(gradeId: Int): Int {
        for (grade in gradeList) {
            if (grade.id == gradeId) {
                gradeList.remove(grade)
                return 1
            }
        }
        return 0
    }

    override suspend fun updateGrade(grade: Grade) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSubject(subjectId: Int) {
        for (subject in subjectList) {
            if(subject.id == subjectId) {
                subjectList.remove(subject)
                return
            }
        }
    }

    override suspend fun deleteAllSubjectSpecificGrades(subjectId: Int) {
        gradeList.forEachIndexed() { index, it ->
            println()
            println("Grade: $it")
            if (it.subjectId == subjectId) {
                println()
                println("Grade removed: $it")
                println()
                gradeList.removeAt(index)
            }
        }
    }


}