package com.mnowo.offlineschoolmanager.feature_todo.domain.use_case

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDoResult
import com.mnowo.offlineschoolmanager.feature_todo.domain.repository.ToDoRepository
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.ToDoValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddToDoUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    operator fun invoke(toDo: ToDo): Flow<Resource<ToDoResult>> = flow<Resource<ToDoResult>> {

        when (ToDoValidation.validateToDo(toDo = toDo)) {
            is ToDoResult.EmptyTitle -> {
                emit(
                    Resource.Error<ToDoResult>(
                        message = "",
                        data = ToDoResult.EmptyTitle
                    )
                )
                return@flow
            }
            is ToDoResult.EmptyDescription -> {
                emit(
                    Resource.Error<ToDoResult>(
                        message = "",
                        data = ToDoResult.EmptyDescription
                    )
                )
                return@flow
            }
            is ToDoResult.DateNotPicked -> {
                emit(
                    Resource.Error<ToDoResult>(
                        message = "",
                        data = ToDoResult.DateNotPicked
                    )
                )
                return@flow
            }
            is ToDoResult.SubjectNotPicked -> {
                emit(
                    Resource.Error<ToDoResult>(
                        message = "",
                        data = ToDoResult.SubjectNotPicked
                    )
                )
                return@flow
            }
            is ToDoResult.Success -> {
                toDoRepository.addToDo(toDo = toDo)
                emit(
                    Resource.Success(
                        data = ToDoResult.Success
                    )
                )
                return@flow
            }
        }
    }.flowOn(Dispatchers.IO)
}