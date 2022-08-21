package com.mnowo.offlineschoolmanager.core.feature_core.domain

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Helper @Inject constructor(
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase
) {

    suspend fun getAllSubjectUseCaseResultHandler(
        onSuccess: () -> Unit,
        onLoading: () -> Unit,
        onError: () -> Unit,
        data: (List<Subject>) -> Unit
    ) {
        getAllSubjectsUseCase.invoke().collect() {
            when (it) {
                is Resource.Success -> {
                   onSuccess()
                }
                is Resource.Loading -> {
                    onLoading()
                    it.data?.let { result ->
                       data(result)
                    }
                }
                is Resource.Error -> {
                   onError()
                }
            }
        }
    }
}