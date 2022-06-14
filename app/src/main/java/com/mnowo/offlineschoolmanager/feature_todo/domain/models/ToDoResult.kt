package com.mnowo.offlineschoolmanager.feature_todo.domain.models

sealed class ToDoResult {
    object EmptyTitle : ToDoResult()
    object EmptyDescription : ToDoResult()
    object SubjectNotPicked : ToDoResult()
    object Success : ToDoResult()
}