package com.mnowo.offlineschoolmanager.feature_todo.data.repository

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_todo.data.local.ToDoDao
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao
) : ToDoRepository {

    override suspend fun addToDo(toDo: ToDo) {
        return toDoDao.addToDo(toDo = toDo)
    }

    override fun getAllToDos(): Flow<List<ToDo>> {
        return toDoDao.getAllToDos()
    }

    override suspend fun updateIsChecked(toDoId: Int, newValue: Boolean) {
        return toDoDao.updateIsChecked(toDoId = toDoId, newValue = newValue)
    }

    override suspend fun deleteToDo(toDoId: Int) {
        return toDoDao.deleteToDo(toDoId = toDoId)
    }

    override suspend fun updateToDo(toDo: ToDo) {
        return toDoDao.updateToDo(toDo = toDo)
    }


}