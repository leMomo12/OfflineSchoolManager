package com.mnowo.offlineschoolmanager.feature_todo.presentation

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import java.util.*

sealed class ToDoEvent {
    data class EnteredTitle(var title: String) : ToDoEvent()
    data class EnteredDescription(var description: String) : ToDoEvent()
    data class ChangeDatePickerState(var isActive: Boolean) : ToDoEvent()
    data class EnteredDate(var date: Date) : ToDoEvent()
    data class ChangeSubjectPickerDialogState(var isActive: Boolean) : ToDoEvent()
    data class ChangePickedSubject(var subject: Subject) : ToDoEvent()
}
