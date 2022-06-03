package com.mnowo.offlineschoolmanager.feature_todo.presentation

import java.util.*

sealed class ToDoEvent {
    data class EnteredDescription(var description: String) : ToDoEvent()
    data class ChangeDatePickerState(var isActive: Boolean) : ToDoEvent()
    data class EnteredDate(var date: Date) : ToDoEvent()
    data class ChangeSubjectPickerDialogState(var isActive: Boolean) : ToDoEvent()
}
