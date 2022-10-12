package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.use_case

import android.content.Context
import android.util.Log.d
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.SubjectResult
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class AddSubjectUseCase @Inject constructor(
    private val repo: SubjectRepository,
    private val context: Context
) {

    operator fun invoke(subject: Subject): Flow<Resource<SubjectResult>> = flow {
        try {
            emit(Resource.Loading<SubjectResult>())
            if(subject.subjectName.trim().isBlank()) {
                emit(
                    Resource.Error<SubjectResult>(
                        message = context.getString(R.string.emptySubject),
                        data = SubjectResult.EmptySubjectText
                    )
                )
                return@flow
            }
            repo.addSubject(subject = subject)
            emit(Resource.Success<SubjectResult>(data = SubjectResult.Success))
        } catch (e: Exception) {
            emit(
                Resource.Error<SubjectResult>(
                    message = (e.localizedMessage ?: R.string.unexpectedError).toString(),
                    data = SubjectResult.ErrorOccurred(message = null)
                )
            )
        }
    }
}