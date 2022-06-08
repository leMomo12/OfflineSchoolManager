package com.mnowo.offlineschoolmanager.feature_todo.domain.use_case

import com.mnowo.offlineschoolmanager.feature_todo.domain.repository.ToDoRepository
import javax.inject.Inject

class UpdateIsCheckedUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    suspend operator fun invoke(toDoId: Int, newValue: Boolean) =
        toDoRepository.updateIsChecked(toDoId = toDoId, newValue = newValue)

}