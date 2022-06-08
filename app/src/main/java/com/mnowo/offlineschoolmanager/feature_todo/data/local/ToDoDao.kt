package com.mnowo.offlineschoolmanager.feature_todo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDo(toDo: ToDo)

    @Query("SELECT * FROM ${Constants.TODO_TABLE}")
    fun getAllToDos() : Flow<List<ToDo>>

    @Query("UPDATE ${Constants.TODO_TABLE} SET isChecked = :newValue WHERE id = :toDoId")
    suspend fun updateIsChecked(toDoId: Int, newValue: Boolean)

}