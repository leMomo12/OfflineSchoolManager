package com.mnowo.offlineschoolmanager.feature_todo.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(

) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _titleState = mutableStateOf<TextFieldState>(TextFieldState())
    val titleState: State<TextFieldState> = _titleState

    private val _descriptionState = mutableStateOf<TextFieldState>(TextFieldState())
    val descriptionState: State<TextFieldState> = _descriptionState

    // DatePicker state
    private val _datePickerState = mutableStateOf<Boolean>(false)
    val datePickerState: State<Boolean> = _datePickerState

    private val _datePickerDateState = mutableStateOf<Date>(value = Calendar.getInstance().time)
    val datePickerDateState: State<Date> = _datePickerDateState

    private val _subjectPickerDialogState = mutableStateOf<Boolean>(false)
    val subjectPickerDialogState: State<Boolean> = _subjectPickerDialogState

    private val _pickedSubjectState =
        mutableStateOf<Subject>(Subject(-1, "", 0, "123", 50.0, 50.0, 1.0))
    val pickedSubjectState: State<Subject> = _pickedSubjectState

    fun bottomNav(screen: Screen, currentScreen: Screen) {
        viewModelScope.launch {
            if (screen != currentScreen) {
                _eventFlow.emit(
                    UiEvent.Navigate(screen.route)
                )
            }
        }
    }

    fun onEvent(event: ToDoEvent) {
        when (event) {
            is ToDoEvent.EnteredTitle -> {
                _titleState.value = titleState.value.copy(
                    text = event.title
                )
            }
            is ToDoEvent.EnteredDescription -> {
                _descriptionState.value = descriptionState.value.copy(
                    text = event.description
                )
            }
            is ToDoEvent.ChangeDatePickerState -> {
                _datePickerState.value = event.isActive
            }
            is ToDoEvent.EnteredDate -> {
                _datePickerDateState.value = event.date
            }
            is ToDoEvent.ChangeSubjectPickerDialogState -> {
                _subjectPickerDialogState.value = event.isActive
            }
            is ToDoEvent.ChangePickedSubject -> {
                _pickedSubjectState.value = event.subject
            }
        }
    }

    fun formatDateToString(): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.US).format(datePickerDateState.value)
    }
}
