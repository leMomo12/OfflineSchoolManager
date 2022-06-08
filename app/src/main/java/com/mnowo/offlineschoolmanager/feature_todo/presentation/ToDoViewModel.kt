package com.mnowo.offlineschoolmanager.feature_todo.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen.SubjectEvent
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.AddToDoUseCase
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.GetAllToDosUseCase
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.UpdateIsCheckedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase,
    private val addToDoUseCase: AddToDoUseCase,
    private val getAllToDosUseCase: GetAllToDosUseCase,
    private val updateIsCheckedUseCase: UpdateIsCheckedUseCase
) : ViewModel() {

    private val _subjectList = mutableStateOf<ListState<Subject>>(ListState())
    val subjectList: State<ListState<Subject>> = _subjectList

    private val _toDoList = mutableStateOf<ListState<ToDo>>(ListState())
    val toDoList: State<ListState<ToDo>> = _toDoList

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

    init {
        getAllToDos()

        getAllSubjects()
    }

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
            is ToDoEvent.OnSubjectDataReceived -> {
                _subjectList.value = subjectList.value.copy(
                    listData = event.data
                )
            }
            is ToDoEvent.OnToDoDataReceived -> {
                _toDoList.value = toDoList.value.copy(
                    listData = event.data
                )
            }
            is ToDoEvent.OnCheckboxChanged -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateIsCheckedUseCase.invoke(toDoId = event.toDoId, newValue = event.newValue)
                }
            }
            is ToDoEvent.AddToDoEvent -> {
                viewModelScope.launch {
                    val toDo = ToDo(
                        id = 0,
                        title = titleState.value.text,
                        description = descriptionState.value.text,
                        until = datePickerDateState.value.time,
                        isChecked = false,
                        subjectId = pickedSubjectState.value.id
                    )
                    addToDoUseCase.invoke(toDo = toDo).collect() {

                    }
                }
            }
        }
    }

    private fun getAllSubjects() = viewModelScope.launch {
        getAllSubjectsUseCase.invoke().collect() {
            when (it) {
                is Resource.Success -> {}
                is Resource.Loading -> {
                    it.data?.let { result ->
                        onEvent(ToDoEvent.OnSubjectDataReceived(data = result))
                    }
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun getAllToDos() = viewModelScope.launch {
        getAllToDosUseCase.invoke().collect() {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { result ->
                        onEvent(ToDoEvent.OnToDoDataReceived(data = result))
                    }
                }
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }
    }
}
