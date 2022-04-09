package com.mnowo.offlineschoolmanager.feature_grade.data.repository


import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GradeRepositoryImpl @Inject constructor(
    private val gradeDao: GradeDao
) : GradeRepository {

    override fun addGrade(grade: Grade) {
        return gradeDao.addGrade(grade = grade)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return gradeDao.getAllSubjects()
    }

    override fun getAllGrades(subjectId: Int): Flow<List<Grade>> {
        return gradeDao.getAllGrades(subjectId = subjectId)
    }


}