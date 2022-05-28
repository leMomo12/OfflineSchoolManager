package com.mnowo.offlineschoolmanager.feature_todo.data.repository

import com.mnowo.offlineschoolmanager.feature_todo.data.local.ToDoDao
import com.mnowo.offlineschoolmanager.feature_todo.domain.repository.ToDoRepository
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao
) : ToDoRepository {

}