package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Helper
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.ConvertDay.convertDayToInt
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.ConvertDay.convertIntToDay
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.TimetableResult
import com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val helper: Helper,
    private val addTimetableItemUseCase: AddTimetableItemUseCase,
    private val getAllTimetableItemsUseCase: GetAllTimetableItemsUseCase,
    private val updateTimetableItemUseCase: UpdateTimetableItemUseCase,
    private val deleteTimetableItemUseCase: DeleteTimetableItemUseCase,
    private val deleteEntireTimetableUseCase: DeleteEntireTimetableUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _timetableListState = mutableStateOf<ListState<Timetable>>(ListState())
    val timetableListState: State<ListState<Timetable>> = _timetableListState

    private val _hourPickerState = mutableStateOf<Int>(1)
    val hourPickerState: State<Int> = _hourPickerState

    private val _subjectDialogState = mutableStateOf<Boolean>(false)
    val subjectDialogState: State<Boolean> = _subjectDialogState

    private val _pickedSubjectState = mutableStateOf<Subject?>(null)
    val pickedSubjectState: State<Subject?> = _pickedSubjectState

    private val _subjectListState = mutableStateOf<ListState<Subject>>(ListState())
    val subjectListState: State<ListState<Subject>> = _subjectListState

    private val _pickSubjectErrorState = mutableStateOf<Color>(Color.LightGray)
    val pickSubjectErrorState: State<Color> = _pickSubjectErrorState

    private val _alreadyTakenErrorState = mutableStateOf<Color>(Color.White)
    val alreadyTakenErrorState: State<Color> = _alreadyTakenErrorState

    private val _pickedDayColorState = mutableStateMapOf<Int, Color>(
        0 to Color.LightGray,
        1 to Color.LightGray,
        2 to Color.LightGray,
        3 to Color.LightGray,
        4 to Color.LightGray
    )
    val pickedDayColorState: SnapshotStateMap<Int, Color> = _pickedDayColorState

    private val _timetableBottomSheetState = mutableStateOf<Boolean>(false)
    val timetableBottomSheetState: State<Boolean> = _timetableBottomSheetState

    private val _dropdownMenuState = mutableStateOf<Boolean>(false)
    val dropdownMenuState: State<Boolean> = _dropdownMenuState

    private val _editState = mutableStateOf<Boolean>(false)
    val editState: State<Boolean> = _editState

    private val _deleteState = mutableStateOf<Boolean>(false)
    val deleteState: State<Boolean> = _deleteState

    private val _deleteAllItemsState = mutableStateOf<Boolean>(false)
    val deleteAllItemsState: State<Boolean> = _deleteAllItemsState

    private val _timetableSpecificItem = mutableStateOf<Timetable?>(null)
    val timetableSpecificItem: State<Timetable?> = _timetableSpecificItem

    private val _deleteDialogState = mutableStateOf<Boolean>(false)
    val deleteDialogState: State<Boolean> = _deleteDialogState

    fun bottomNav(screen: Screen, currentScreen: Screen) {
        viewModelScope.launch {
            if (screen != currentScreen) {
                _eventFlow.emit(
                    UiEvent.Navigate(screen.route)
                )
            }
        }
    }

    init {
        getAllTimetableItems()
        getAllSubjects()
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
            is TimetableEvent.SetTimetableBottomSheet -> {
                _timetableBottomSheetState.value = event.value
            }
            is TimetableEvent.SetPickSubjectError -> {
                _pickSubjectErrorState.value = event.value
            }
            is TimetableEvent.SetTimetableList -> {
                _timetableListState.value = timetableListState.value.copy(
                    listData = event.listData
                )
            }
            is TimetableEvent.SetAlreadyTakenErrorState -> {
                _alreadyTakenErrorState.value = event.value
            }
            is TimetableEvent.SetDropdownMenuState -> {
                _dropdownMenuState.value = event.value
            }
            is TimetableEvent.SetEditState -> {
                _editState.value = event.value
            }
            is TimetableEvent.SetDeleteState -> {
                _deleteState.value = event.value
            }
            is TimetableEvent.SetDeleteAllItemsState -> {
                _deleteAllItemsState.value = event.value
            }
            is TimetableEvent.SetDeleteDialogState -> {
                _deleteDialogState.value = event.value
            }
            is TimetableEvent.OnEditClicked -> {
                val subject = searchForSubject(
                    subjectId = event.timetableItem.subjectId,
                    timetableId = event.timetableItem.id
                )
                val intDay = convertDayToInt(day = event.timetableItem.day)
                dayColorChange(day = intDay)
                onEvent(TimetableEvent.OnSubjectPicked(subject = subject))
                onEvent(TimetableEvent.OnHourPickerChanged(hour = event.timetableItem.hour))
            }
            is TimetableEvent.SetTimetableSpecificItem -> {
                _timetableSpecificItem.value = event.timetable
            }
            is TimetableEvent.AddTimetable -> {
                viewModelScope.launch(Dispatchers.IO) {
                    removeAllErrors()
                    val day = getDay()
                    pickedSubjectState.value?.let { subject ->
                        val timetable = Timetable(0, day, hourPickerState.value, subject.id)

                        addTimetableItemUseCase.invoke(
                            timetable = timetable,
                            timetableList = timetableListState.value.listData
                        ).collect {
                            addOrEditTimetableEventResult(result = it)
                        }
                    } ?: onEvent(TimetableEvent.SetPickSubjectError(value = Color.Red))
                }
            }
            is TimetableEvent.UpdateTimetableItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    removeAllErrors()
                    timetableSpecificItem.value?.let { timetable ->
                        pickedSubjectState.value?.let { subject ->
                            val day = getDay()
                            val timetableItem = Timetable(
                                id = timetable.id,
                                day = day,
                                hour = hourPickerState.value,
                                subjectId = subject.id
                            )
                            updateTimetableItemUseCase.invoke(
                                timetable = timetableItem,
                                timetableList = timetableListState.value.listData
                            ).collect() {
                                addOrEditTimetableEventResult(result = it)
                            }
                        } ?: onEvent(TimetableEvent.SetPickSubjectError(Color.Red))
                    }
                }
            }
            is TimetableEvent.DeleteTimetableItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    timetableSpecificItem.value?.let { timetable ->
                        deleteTimetableItemUseCase.invoke(timetable = timetable)
                        onEvent(TimetableEvent.SetDeleteDialogState(false))
                    }
                }
            }
            is TimetableEvent.DeleteEntireTimetable -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteEntireTimetableUseCase.invoke()
                }
            }
        }
    }

    private fun addOrEditTimetableEventResult(result: Resource<TimetableResult>) {
        when (result) {
            is Resource.Error -> {
                when (result.data) {
                    is TimetableResult.EmptyDay -> {
                        setDayErrorColor()
                    }
                    is TimetableResult.AlreadyTaken -> {
                        _alreadyTakenErrorState.value = Color.Red
                    }

                    else -> {}
                }
            }
            is Resource.Success -> {
                d("Timetable", "success ")
                onEvent(TimetableEvent.SetTimetableBottomSheet(false))
                removeAllErrors()
            }

            else -> {}
        }
    }

    private fun getAllTimetableItems() = viewModelScope.launch {
        getAllTimetableItemsUseCase.invoke().collect() {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { listData ->
                        onEvent(TimetableEvent.SetTimetableList(listData))
                    }
                }

                else -> {}
            }
        }
    }

    private fun dayColorChange(day: Int) = viewModelScope.launch {
        for (i in 0 until 5) {
            pickedDayColorState[i] = Color.LightGray
        }
        pickedDayColorState[day] = LightBlue
    }

    private fun setDayErrorColor() {
        for (day in 0 until 5) {
            pickedDayColorState[day] = Color.Red
        }
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

    private fun removeAllErrors() {
        _pickSubjectErrorState.value = Color.LightGray
        _alreadyTakenErrorState.value = Color.White
    }


    fun searchIfTimetableItemExists(hour: Int, intDay: Int): Timetable {
        val day = convertIntToDay(day = intDay)
        val timetable = Timetable(-1, day, hour, -1)
        val item =
            timetableListState.value.listData.filter { (it.day == day) && (it.hour == hour) }


        if (item.isEmpty()) {
            return timetable
        } else {
            return timetable.copy(
                id = item[0].id,
                day = item[0].day,
                hour = item[0].hour,
                subjectId = item[0].subjectId
            )
        }
    }

    fun searchForSubject(subjectId: Int, timetableId: Int): Subject {
        val subject = Subject(
            -1,
            "",
            Color.LightGray.toArgb(),
            "",
            50.0,
            50.0,
            1.0
        )
        if (timetableId == -1) return subject

        val item = subjectListState.value.listData.filter { it.id == subjectId }

        if (item.isEmpty()) {
            return subject
        } else {
            return subject.copy(
                id = item[0].id,
                subjectName = item[0].subjectName,
                color = item[0].color,
                room = item[0].room,
                oralPercentage = item[0].oralPercentage,
                writtenPercentage = item[0].writtenPercentage,
                average = item[0].average
            )
        }
    }
}