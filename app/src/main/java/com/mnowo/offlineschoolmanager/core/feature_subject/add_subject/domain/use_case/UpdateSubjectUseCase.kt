package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.SubjectResult
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.repository.SubjectRepository
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.use_case.util.SubjectValidation
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateSubjectUseCase @Inject constructor(
    private val repository: SubjectRepository
) {

    operator fun invoke(subject: Subject): Flow<Resource<SubjectResult>> =
        flow<Resource<SubjectResult>> {
            when (val validationResult = SubjectValidation.validateSubject(subject = subject)) {
                is SubjectResult.EmptySubjectText -> {
                    emit(
                        Resource.Error(
                            message = "Empty subject text",
                            data = SubjectResult.EmptySubjectText
                        )
                    )
                }
                is SubjectResult.DoesntAddUpTo100 -> {
                    emit(
                        Resource.Error(
                            message = "Doesn't add zp to 100%",
                            data = SubjectResult.DoesntAddUpTo100
                        )
                    )
                }
                is SubjectResult.ErrorOccurred -> {
                    emit(
                        Resource.Error(
                            message = "Empty subject text",
                            data = SubjectResult.ErrorOccurred(message = validationResult.message)
                        )
                    )
                }
                is SubjectResult.Success -> {
                    repository.updateSubject(subject = subject)
                    emit(Resource.Success(data = SubjectResult.Success))
                    return@flow
                }
            }
        }.flowOn(Dispatchers.IO)
}