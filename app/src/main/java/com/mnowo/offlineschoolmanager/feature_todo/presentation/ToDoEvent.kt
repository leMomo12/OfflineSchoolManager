package com.mnowo.offlineschoolmanager.feature_todo.presentation

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import java.util.*

sealed class ToDoEvent {
    data class EnteredTitle(var title: String) : ToDoEvent()
    data class EnteredDescription(var description: String) : ToDoEvent()
    data class ChangeDatePickerState(var isActive: Boolean) : ToDoEvent()
    data class EnteredDate(var date: Date) : ToDoEvent()
    data class ChangeSubjectPickerDialogState(var isActive: Boolean) : ToDoEvent()
    data class ChangePickedSubject(var subject: Subject) : ToDoEvent()
    data class OnSubjectDataReceived(var data: List<Subject>) : ToDoEvent()
    data class OnToDoDataReceived(var data: List<ToDo>) : ToDoEvent()
    data class OnCheckboxChanged(var toDoId: Int, var newValue: Boolean) : ToDoEvent()
    data class ChangeBottomSheetState(var shows: Boolean) : ToDoEvent()
    object AddToDoEvent : ToDoEvent()

}
