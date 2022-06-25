package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.Helper
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val helper: Helper
) : ViewModel() {

    init {
        getAllSubjects()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _hourPickerState = mutableStateOf<Int>(0)
    val hourPickerState: State<Int> = _hourPickerState

    private val _subjectDialogState = mutableStateOf<Boolean>(false)
    val subjectDialogState: State<Boolean> = _subjectDialogState

    private val _pickedSubjectState = mutableStateOf<Subject?>(null)
    val pickedSubjectState: State<Subject?> = _pickedSubjectState

    private val _subjectListState = mutableStateOf<ListState<Subject>>(ListState())
    val subjectListState: State<ListState<Subject>> = _subjectListState

    private val _pickedDayColorState = mutableStateOf<Array<Color>>(
        arrayOf(
            Color.LightGray,
            Color.LightGray,
            Color.LightGray,
            Color.LightGray,
            Color.LightGray,
        )
    )
    val pickedDayColorState: State<Array<Color>> = _pickedDayColorState

    fun bottomNav(screen: Screen, currentScreen: Screen) {
        viewModelScope.launch {
            if (screen != currentScreen) {
                _eventFlow.emit(
                    UiEvent.Navigate(screen.route)
                )
            }
        }
    }

    fun onEvent(event: TimetableEvent) {
        when (event) {
            is TimetableEvent.OnHourPickerChanged -> {
                _hourPickerState.value = event.hour
            }
            is TimetableEvent.OnSubjectDialogStateChanged -> {
                _subjectDialogState.value = event.value
            }
            is TimetableEvent.OnSubjectPicked -> {
                _pickedSubjectState.value = event.subject
            }
            is TimetableEvent.OnSubjectDataReceived -> {
                _subjectListState.value = subjectListState.value.copy(
                    listData = event.listData
                )
            }
            is TimetableEvent.OnPickedDayColorStateChanged -> {

            }
        }
    }

    fun getAllSubjects() = viewModelScope.launch {
        helper.getAllSubjectUseCaseResultHandler(
            onSuccess = {},
            onLoading = {},
            onError = {},
            data = { onEvent(TimetableEvent.OnSubjectDataReceived(listData = it)) }
        )
    }
}