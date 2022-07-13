package com.mnowo.offlineschoolmanager.feature_home.presentation

import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.CalculateGradeColor
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.RoundOffDecimals
import com.mnowo.offlineschoolmanager.feature_home.domain.use_case.GetAverageUseCase
import com.mnowo.offlineschoolmanager.feature_home.domain.use_case.GetCountOfSubjectsUseCase
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.GetAllTimetableItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAverageUseCase: GetAverageUseCase,
    private val getAllTimetableItemsUseCase: GetAllTimetableItemsUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _averageState = mutableStateOf<Double>(0.0)
    val averageState: State<Double> = _averageState

    private val _gradeColorState = mutableStateOf<Color>(Color.LightGray)
    val gradeColorState: State<Color> = _gradeColorState

    private val _currentTimeState = mutableStateOf<String>("")
    val currentTimeState: State<String> = _currentTimeState

    private val _timetableListState = mutableStateOf<ListState<Timetable>>(ListState())
    val timetableListState: State<ListState<Timetable>> = _timetableListState

    private val _dailyTimetableListState = mutableStateOf<ListState<Timetable>>(ListState())
    val dailyTimetableListState: State<ListState<Timetable>> = _dailyTimetableListState

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SetAverageState -> {
                _averageState.value = event.value
            }
            is HomeEvent.SetGradeColorState -> {
                _gradeColorState.value = event.color
            }
            is HomeEvent.SetTimetableListState -> {

            }
        }
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

    fun getStartingInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            getAverage()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentDate()
            getDailyTimetable()
        }
    }

    private suspend fun getAverage() {
        val getAverageList = getAverageUseCase.invoke()

        var sum = 0.0
        var countOfSubjects = 0
        for (item in getAverageList) {
            if (item != 0.0) {
                countOfSubjects++
            }
            sum += item
        }

        if (sum == 0.0) {
            return
        } else {

            val average = sum / countOfSubjects
            val roundedAverage = RoundOffDecimals.roundOffDoubleDecimals(grade = average)
            onEvent(HomeEvent.SetAverageState(value = roundedAverage))


            val calculatedColor = CalculateGradeColor.calculateGradeColor(grade = average)
            onEvent(
                HomeEvent.SetGradeColorState(
                    color = Color(
                        red = calculatedColor.red,
                        green = calculatedColor.green,
                        blue = calculatedColor.blue
                    )
                )
            )
        }
    }

    private fun getCurrentDate() {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        _currentTimeState.value = dateFormat.format(date)
    }

    private suspend fun getDailyTimetable() {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2
        d("DayOfWeek", "day: $day")

        getAllTimetableItemsUseCase.invoke().collect() {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { data ->
                        onEvent(HomeEvent.SetTimetableListState(listData = data))
                        d("DailyList", "data")
                    }
                }
            }
            val list = timetableListState.value.listData.filter { it.day.day == day}.sortedBy { it.id }
            onEvent(HomeEvent.SetTimetableListState(listData = list))
        }
    }
}