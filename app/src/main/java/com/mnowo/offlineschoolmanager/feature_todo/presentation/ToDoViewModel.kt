package com.mnowo.offlineschoolmanager.feature_todo.presentation

import android.util.Log.d
import android.util.Log.i
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.Helper
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
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDoResult
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.*
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
    private val updateIsCheckedUseCase: UpdateIsCheckedUseCase,
    private val deleteToDoUseCase: DeleteToDoUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val helper: Helper
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

    private val _bottomSheetState = mutableStateOf<Boolean>(value = false)
    val bottomSheetState: State<Boolean> = _bottomSheetState

    private val _titleErrorState = mutableStateOf<Boolean>(false)
    val titleErrorState: State<Boolean> = _titleErrorState

    private val _descriptionErrorState = mutableStateOf<Boolean>(false)
    val descriptionErrorState: State<Boolean> = _descriptionErrorState

    private val _pickSubjectColorState = mutableStateOf<Color>(Color.LightGray)
    val pickSubjectColorState: State<Color> = _pickSubjectColorState

    private val _dropDownMenuState = mutableStateOf<Boolean>(false)
    val dropDownMenuState: State<Boolean> = _dropDownMenuState

    private val _editState = mutableStateOf<Boolean>(false)
    val editState: State<Boolean> = _editState

    private val _deleteState = mutableStateOf<Boolean>(false)
    val deleteState: State<Boolean> = _deleteState

    private val _deleteDialogState = mutableStateOf<Boolean>(false)
    val deleteDialogState: State<Boolean> = _deleteDialogState

    private val _deleteToDoIdState = mutableStateOf<Int>(-1)
    val deleteToDoIdState: State<Int> = _deleteToDoIdState

    private val _contentEditState = mutableStateOf<Boolean>(false)
    val contentEditState: State<Boolean> = _contentEditState

    private val _specificEditToDoState = mutableStateOf<ToDo?>(null)
    val specificEditToDoState: State<ToDo?> = _specificEditToDoState

    fun setContentEditState(value: Boolean) {
        _contentEditState.value = value
    }

    fun setDeleteToDoIdState(value: Int) {
        _deleteToDoIdState.value = value
    }

    private fun setTitleErrorState(value: Boolean) {
        _titleErrorState.value = value
    }

    private fun setDescriptionErrorState(value: Boolean) {
        _descriptionErrorState.value = value
    }

    private fun setPickSubjectColorState(value: Color) {
        _pickSubjectColorState.value = value
    }

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
            is ToDoEvent.ChangeBottomSheetState -> {
                _bottomSheetState.value = event.shows
            }
            is ToDoEvent.ChangeDropDownMenuState -> {
                _dropDownMenuState.value = event.value
            }
            is ToDoEvent.ChangeEditState -> {
                _editState.value = event.value
            }
            is ToDoEvent.ChangeDeleteState -> {
                _deleteState.value = event.value
            }
            is ToDoEvent.ChangeDeleteDialogState -> {
                _deleteDialogState.value = event.value
            }
            is ToDoEvent.ChangeSpecificEditToDoState -> {
                _specificEditToDoState.value = event.value
            }
            is ToDoEvent.EditToDo -> {
                viewModelScope.launch {
                    specificEditToDoState.value?.let {
                        val toDo = ToDo(
                            id = it.id,
                            title = titleState.value.text,
                            description = descriptionState.value.text,
                            until = datePickerDateState.value.time,
                            isChecked = it.isChecked,
                            subjectId = pickedSubjectState.value.id
                        )

                        updateToDoUseCase.invoke(toDo = toDo).collect() { result ->
                            when (result) {
                                is Resource.Success -> {
                                    addOrEditToDoSuccess()
                                }
                                is Resource.Error -> {
                                    addOrEditToDoError(toDoResult = result.data)
                                }
                            }
                        }
                    }
                }
            }
            is ToDoEvent.DeleteToDo -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteToDoUseCase.invoke(toDoId = _deleteToDoIdState.value)
                }
            }
            is ToDoEvent.AddToDoEvent -> {
                viewModelScope.launch {
                    removeAllErrors()
                    val toDo = ToDo(
                        id = 0,
                        title = titleState.value.text,
                        description = descriptionState.value.text,
                        until = datePickerDateState.value.time,
                        isChecked = false,
                        subjectId = pickedSubjectState.value.id
                    )
                    addToDoUseCase.invoke(toDo = toDo).collect() {
                        when (it) {
                            is Resource.Success -> {
                                addOrEditToDoSuccess()
                            }
                            is Resource.Error -> {
                                addOrEditToDoError(toDoResult = it.data)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getAllSubjects() = viewModelScope.launch {
        helper.getAllSubjectUseCaseResultHandler(
            onSuccess = {},
            onLoading = {},
            onError = {},
            data = { onEvent(ToDoEvent.OnSubjectDataReceived(data = it)) })
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

    private fun addOrEditToDoSuccess() = viewModelScope.launch {
        clearAfterToDoEvent()
        removeAllErrors()

        onEvent(ToDoEvent.ChangeBottomSheetState(false))
    }

    private fun addOrEditToDoError(toDoResult: ToDoResult?) =
        viewModelScope.launch {
            when (toDoResult) {
                is ToDoResult.Success -> {}
                is ToDoResult.SubjectNotPicked -> {
                    setPickSubjectColorState(Color.Red)
                }
                is ToDoResult.EmptyTitle -> {
                    setTitleErrorState(true)
                }
                is ToDoResult.EmptyDescription -> {
                    setDescriptionErrorState(true)
                }
            }
        }

    private fun clearAfterToDoEvent() {
        _titleState.value.clearText()
        _descriptionState.value.clearText()
        _datePickerDateState.value = Calendar.getInstance().time
        _pickedSubjectState.value =
            Subject(-1, "", 0, "123", 50.0, 50.0, 1.0)
    }

    private fun removeAllErrors() {
        setTitleErrorState(false)
        setDescriptionErrorState(false)
        setPickSubjectColorState(Color.LightGray)
    }
}
