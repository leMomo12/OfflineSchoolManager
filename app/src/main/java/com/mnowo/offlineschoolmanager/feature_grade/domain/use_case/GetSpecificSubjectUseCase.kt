package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSpecificSubjectUseCase @Inject constructor(
  private val repository: GradeRepository
) {

    operator fun invoke(subjectId: Int) : Flow<Subject> = flow<Subject> {
        emit(repository.getSpecificSubject(subjectId = subjectId))
    }.flowOn(Dispatchers.IO)
}