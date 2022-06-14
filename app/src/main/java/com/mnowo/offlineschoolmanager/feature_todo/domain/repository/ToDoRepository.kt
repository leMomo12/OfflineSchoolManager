package com.mnowo.offlineschoolmanager.feature_todo.domain.repository

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    suspend fun addToDo(toDo: ToDo)

    fun getAllToDos() : Flow<List<ToDo>>

    suspend fun updateIsChecked(toDoId: Int, newValue: Boolean)

    suspend fun deleteToDo(toDoId: Int)

    suspend fun updateToDo(toDo: ToDo)
}