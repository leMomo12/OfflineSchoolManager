package com.mnowo.offlineschoolmanager.feature_todo.domain.use_case

import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllToDosUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    operator fun invoke(): Flow<Resource<List<ToDo>>> = flow<Resource<List<ToDo>>> {
        try {
            toDoRepository.getAllToDos().collect() {
                emit(Resource.Loading(data = it))
                emit(Resource.Success(data = it))
            }
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = (e.localizedMessage ?: R.string.unexpectedError).toString()
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}