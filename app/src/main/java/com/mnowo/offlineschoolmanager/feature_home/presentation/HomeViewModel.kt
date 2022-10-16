package com.mnowo.offlineschoolmanager.feature_home.presentation

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Helper
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.use_case.GetAllExamItemsUseCase
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.CalculateGradeColor
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.ConvertDay.convertDayToInt
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.ExamSubjectItemHelper
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.RoundOffDecimals
import com.mnowo.offlineschoolmanager.feature_home.domain.use_case.GetAverageUseCase
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.domain.use_case.GetAllTimetableItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAverageUseCase: GetAverageUseCase,
    private val getAllTimetableItemsUseCase: GetAllTimetableItemsUseCase,
    private val helper: Helper,
    private val getAllExamItemsUseCase: GetAllExamItemsUseCase,
    @ApplicationContext context: Context
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

    private val _dailyTimetableMap = mutableStateMapOf<Timetable, Subject>()
    val dailyTimetableMap: SnapshotStateMap<Timetable, Subject> = _dailyTimetableMap

    private val _subjectListState = mutableStateOf<ListState<Subject>>(ListState())
    val subjectListState: State<ListState<Subject>> = _subjectListState

    private val _isTodayTimetableState = mutableStateOf(true)
    val isTodayTimetableState: State<Boolean> = _isTodayTimetableState

    private val _emptyDailyList = mutableStateOf(true)
    val emptyDailyList: State<Boolean> = _emptyDailyList

    private val _examListState = mutableStateOf<ListState<Exam>>(ListState())
    val examListState: State<ListState<Exam>> = _examListState

    private val _notExpiredExamListState = mutableStateOf<ListState<Exam>>(ListState())
    val notExpiredExamListState: State<ListState<Exam>> = _notExpiredExamListState

    private val _bestAndWorstSubjectState = mutableMapOf<Int, String?>(0 to null, 1 to null)
    val bestAndWorstSubjectState: Map<Int, String?> = _bestAndWorstSubjectState

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SetAverageState -> {
                _averageState.value = event.value
            }
            is HomeEvent.SetGradeColorState -> {
                _gradeColorState.value = event.color
            }
            is HomeEvent.SetTimetableListState -> {
                _timetableListState.value = timetableListState.value.copy(
                    listData = event.listData
                )
            }
            is HomeEvent.AddDailyTimetableListState -> {
                _dailyTimetableListState.value = dailyTimetableListState.value.copy(
                    listData = event.listData
                )
            }
            is HomeEvent.SetSubjectListState -> {
                _subjectListState.value = subjectListState.value.copy(
                    listData = event.listData
                )
            }
            is HomeEvent.SetIsTodayTimetableState -> {
                _isTodayTimetableState.value = event.value
            }
            is HomeEvent.SetEmptyDailyList -> {
                _emptyDailyList.value = event.isEmpty
            }
            is HomeEvent.SetExamListState -> {
                _examListState.value = examListState.value.copy(
                    listData = event.list
                )
            }
            is HomeEvent.SetNotExpiredExamListState -> {
                _notExpiredExamListState.value = notExpiredExamListState.value.copy(
                    listData = event.list
                )
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
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllSubjects()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllTimetableItems()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllExamItems()
        }
    }

    private suspend fun getAllSubjects() {
        helper.getAllSubjectUseCaseResultHandler(
            onSuccess = {},
            onError = {},
            onLoading = {},
            data = {
                onEvent(HomeEvent.SetSubjectListState(listData = it))
                if (timetableListState.value.listData.isNotEmpty()) {
                    getDailyTimetable()
                }
                getBestAndWorstSubject()
            }
        )
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

    private suspend fun getAllTimetableItems() {
        getAllTimetableItemsUseCase.invoke().collect() {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { data ->
                        onEvent(HomeEvent.SetTimetableListState(listData = data))
                        if (subjectListState.value.listData.isNotEmpty()) {
                            getDailyTimetable()
                        }
                    }
                }
            }
        }
    }

    private fun getDailyTimetable() {
        val dailyList = mutableListOf<Timetable>()
        var day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2

        if (timetableListState.value.listData.isNotEmpty()) {
            onEvent(HomeEvent.SetEmptyDailyList(isEmpty = false))
            dailyTimetableLoop(
                day = day,
                addToDailyList = {
                    dailyList.add(it)
                }
            )
            while (dailyList.isEmpty()) {
                day++
                if (day >= 5) {
                    day = 0
                }
                dailyTimetableLoop(
                    day = day,
                    addToDailyList = {
                        dailyList.add(it)
                    }
                )
            }
        }

        dailyList.sortBy { it.hour }
        onEvent(HomeEvent.AddDailyTimetableListState(dailyList))
    }

    private fun dailyTimetableLoop(day: Int, addToDailyList: (Timetable) -> Unit) {
        for (item in timetableListState.value.listData) {
            val intDay = convertDayToInt(day = item.day)
            if (intDay == day) {
                addToDailyList(item)
                try {
                    val subject =
                        subjectListState.value.listData.filter { it.id == item.subjectId }[0]

                    _dailyTimetableMap.set(key = item, value = subject)
                    onEvent(HomeEvent.SetIsTodayTimetableState(true))
                } catch (e: Exception) {
                    return
                }
            }
        }
    }

    private suspend fun getAllExamItems() {
        getAllExamItemsUseCase.invoke().collect() {
            when (it) {
                is Resource.Loading -> {
                    onEvent(HomeEvent.SetExamListState(list = it.data ?: listOf()))
                    getNotExpiredList()
                }
            }
        }
    }

    fun getExamSubjectItem(examData: Exam): Subject {
        return ExamSubjectItemHelper.getSubjectItem(
            examData = examData,
            subjectList = subjectListState.value.listData
        )
    }

    private fun getNotExpiredList() {
        val notExpiredExamList = examListState.value.listData.filter {
            !isExamExpired(examLongDate = it.date)
        }
        onEvent(HomeEvent.SetNotExpiredExamListState(list = notExpiredExamList))
    }

    private fun isExamExpired(examLongDate: Long): Boolean {
        return ExamSubjectItemHelper.isExamExpired(examLongDate = examLongDate)
    }

    private fun getBestAndWorstSubject() {
        if (subjectListState.value.listData.isNotEmpty()) {
            val sortedSubjectList =
                subjectListState.value.listData.filter { it.average != 0.0 }.sortedBy { it.average }

            if (sortedSubjectList.isNotEmpty()) {
                _bestAndWorstSubjectState[0] = sortedSubjectList[0].subjectName
                _bestAndWorstSubjectState[1] = sortedSubjectList.last().subjectName
            }
        }
    }
}



