package com.mnowo.offlineschoolmanager.feature_todo.domain.use_case

import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.repository.ToDoRepository
import javax.inject.Inject

class DeleteToDoUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    suspend operator fun invoke(toDoId: Int) = toDoRepository.deleteToDo(toDoId = toDoId)
}