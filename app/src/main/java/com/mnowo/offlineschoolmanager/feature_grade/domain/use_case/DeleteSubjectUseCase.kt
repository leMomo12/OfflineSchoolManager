package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DeleteSubjectUseCase @Inject constructor(
  private val gradeRepository: GradeRepository
) {

    operator fun invoke(subjectId: Int) = flow<Boolean> {
        gradeRepository.deleteSubject(subjectId = subjectId)
        gradeRepository.deleteAllSubjectSpecificGrades(subjectId = subjectId)
        gradeRepository.deleteAllSubjectsFromTimetable(subjectId = subjectId)
        gradeRepository.deleteAllSubjectsFromToDo(subjectId = subjectId)
        emit(true)
    }.flowOn(Dispatchers.IO)
}