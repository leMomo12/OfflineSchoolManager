package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.Helper
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.AddTimetableItemUseCase
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val helper: Helper,
    private val addTimetableItemUseCase: AddTimetableItemUseCase
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

    private val _pickSubjectErrorState = mutableStateOf<Color>(Color.LightGray)
    val pickSubjectErrorState: State<Color> = _pickSubjectErrorState

    private val _pickedDayColorState = mutableStateMapOf<Int, Color>(
        0 to Color.LightGray,
        1 to Color.LightGray,
        2 to Color.LightGray,
        3 to Color.LightGray,
        4 to Color.LightGray
    )
    val pickedDayColorState: SnapshotStateMap<Int, Color> = _pickedDayColorState

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
                dayColorChange(day = event.day)
            }
            is TimetableEvent.SetPickSubjectError -> {
                _pickSubjectErrorState.value = event.value
            }
            is TimetableEvent.AddTimetable -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val day = getDay()
                    pickedSubjectState.value?.let { subject ->
                        val timetable = Timetable(0, day, hourPickerState.value, subject.id)

                        addTimetableItemUseCase.invoke(timetable = timetable).collect {
                            when (it) {
                                is Resource.Error -> {
                                }
                                is Resource.Success -> {

                                }
                            }
                        }
                    } ?: onEvent(TimetableEvent.SetPickSubjectError(value = Color.Red))
                }
            }
        }
    }

    private fun dayColorChange(day: Int) = viewModelScope.launch {
        for (i in 0 until 5) {
            pickedDayColorState[i] = Color.LightGray
        }
        pickedDayColorState[day] = LightBlue
    }

    private fun getDay(): Days {
        for (intDay in 0 until 5) {
            if (pickedDayColorState[intDay] == LightBlue) {
                return when (intDay) {
                    0 -> Days.MONDAY
                    1 -> Days.TUESDAY
                    2 -> Days.WEDNESDAY
                    3 -> Days.THURSDAY
                    else -> Days.FRIDAY
                }
            }
        }
        return Days.EXCEPTION
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