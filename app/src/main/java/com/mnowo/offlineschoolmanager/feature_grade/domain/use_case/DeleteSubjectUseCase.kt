package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DeleteSubjectUseCase @Inject constructor(
  private val gradeDao: GradeDao
) {

    operator fun invoke(subjectId: Int) = flow<Boolean> {
        gradeDao.deleteSubject(subjectId = subjectId)
        gradeDao.deleteAllSubjectSpecificGrades(subjectId = subjectId)
    }.flowOn(Dispatchers.IO)
}