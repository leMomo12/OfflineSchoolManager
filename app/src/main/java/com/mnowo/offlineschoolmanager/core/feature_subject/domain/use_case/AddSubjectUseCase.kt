package com.mnowo.offlineschoolmanager.core.feature_subject.domain.use_case

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.repository.SubjectRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class AddSubjectUseCase @Inject constructor(
    private val repo: SubjectRepository,
    private val context: Context
) {

    operator fun invoke(subject: Subject): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            if(subject.subjectName.trim().isBlank()) {
                emit(
                    Resource.Error<Boolean>(
                        message = context.getString(R.string.emptySubject)
                    )
                )
                return@flow
            }
            if(subject.oralPercentage.toString().isBlank()) {
                emit(
                    Resource.Error<Boolean>(
                        message = context.getString(R.string.emptyPercentage)
                    )
                )
                return@flow
            }

            if(subject.writtenPercentage.toString().isBlank()) {
                emit(
                    Resource.Error<Boolean>(
                        message = context.getString(R.string.emptyPercentage),
                    )
                )
                return@flow
            }

            repo.addSubject(subject = subject)
            emit(Resource.Success<Boolean>(data = true))
        } catch (e: Exception) {
            emit(
                Resource.Error<Boolean>(
                    message = (e.localizedMessage ?: R.string.unexpectedError).toString()
                )
            )
        }
    }
}