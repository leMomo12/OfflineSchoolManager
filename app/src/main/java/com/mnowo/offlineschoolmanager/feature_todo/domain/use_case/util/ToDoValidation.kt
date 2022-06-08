package com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util

import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDoResult

object ToDoValidation {

    fun validateToDo(toDo: ToDo): ToDoResult {
        return if (toDo.title.trim().isBlank()) {
            ToDoResult.EmptyTitle
        } else if (toDo.description.trim().isBlank()) {
            ToDoResult.EmptyDescription
        } else if (toDo.until == 0L) {
            ToDoResult.DateNotPicked
        } else if (toDo.subjectId == -1) {
            ToDoResult.SubjectNotPicked
        } else {
            ToDoResult.Success
        }
    }
}