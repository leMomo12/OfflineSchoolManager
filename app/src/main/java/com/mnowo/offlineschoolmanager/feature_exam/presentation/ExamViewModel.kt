package com.mnowo.offlineschoolmanager.feature_exam.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.Helper
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.ExamResult
import com.mnowo.offlineschoolmanager.feature_exam.domain.use_case.AddExamUseCase
import com.mnowo.offlineschoolmanager.feature_exam.domain.use_case.GetAllExamItemsUseCase
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen.SubjectEvent
import com.mnowo.offlineschoolmanager.feature_home.presentation.HomeEvent
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val helper: Helper,
    private val getAllExamItemsUseCase: GetAllExamItemsUseCase,
    private val addExamUseCase: AddExamUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _titleState = mutableStateOf<TextFieldState>(TextFieldState())
    val titleState: State<TextFieldState> = _titleState

    private val _descriptionState = mutableStateOf<TextFieldState>(TextFieldState())
    val descriptionState: State<TextFieldState> = _descriptionState

    private val _dateState = mutableStateOf<Date>(value = Calendar.getInstance().time)
    val dateState: State<Date> = _dateState

    private val _datePickerState = mutableStateOf<Boolean>(false)
    val datePickerState: State<Boolean> = _datePickerState

    private val _pickedSubjectState =
        mutableStateOf<Subject>(Subject(-1, "", 0, "123", 50.0, 50.0, 1.0))
    val pickedSubjectState: State<Subject> = _pickedSubjectState

    private val _subjectPickerState = mutableStateOf<Boolean>(false)
    val subjectPickerState: State<Boolean> = _subjectPickerState

    private val _pickedSubjectColorState = mutableStateOf<Color>(Color.LightGray)
    val pickedSubjectColorState: State<Color> = _pickedSubjectColorState

    private val _subjectListState = mutableStateOf<ListState<Subject>>(ListState())
    val subjectListState: State<ListState<Subject>> = _subjectListState

    private val _bottomSheetState = mutableStateOf<Boolean>(false)
    val bottomSheetState: State<Boolean> = _bottomSheetState

    private val _examListState = mutableStateOf<ListState<Exam>>(ListState())
    val examListState: State<ListState<Exam>> = _examListState

    private val _titleErrorState = mutableStateOf<Boolean>(false)
    val titleErrorState: State<Boolean> = _titleErrorState

    private val _descriptionErrorState = mutableStateOf<Boolean>(false)
    val descriptionErrorState: State<Boolean> = _descriptionErrorState

    private val _addExamSubjectIdState = mutableStateOf<Int>(-1)
    val addExamSubjectIdState: State<Int> = _addExamSubjectIdState

    fun onEvent(event: ExamEvent) {
        when (event) {
            is ExamEvent.SetTitleState -> {
                _titleState.value = titleState.value.copy(
                    text = event.text
                )
            }
            is ExamEvent.SetDescriptionState -> {
                _descriptionState.value = descriptionState.value.copy(
                    text = event.text
                )
            }
            is ExamEvent.SetDateState -> {
                _dateState.value = event.date
            }
            is ExamEvent.SetDatePickerState -> {
                _datePickerState.value = event.value
            }
            is ExamEvent.SetSubjectPickerState -> {
                _subjectPickerState.value = event.value
            }
            is ExamEvent.SetPickedSubjectState -> {
                _pickedSubjectState.value = event.subject
            }
            is ExamEvent.SetSubjectListState -> {
                _subjectListState.value = subjectListState.value.copy(
                    listData = event.list
                )
            }
            is ExamEvent.SetAddExamSubjectIdState -> {
                _addExamSubjectIdState.value = event.subjectId
            }
            is ExamEvent.ChangeBottomSheetState -> {
                _bottomSheetState.value = event.value
            }
            is ExamEvent.SetExamListState -> {
                _examListState.value = examListState.value.copy(
                    listData = event.list
                )
            }
            is ExamEvent.SetPickedSubjectColorState -> {
                _pickedSubjectColorState.value = event.color
            }
            is ExamEvent.AddResult -> {
                viewModelScope.launch {
                    onEvent(ExamEvent.SetAddExamSubjectIdState(subjectId = event.subjectId))
                    _eventFlow.emit(
                        UiEvent.Navigate(Screen.GradeScreen.route)
                    )
                }
            }

            is ExamEvent.AddExamItem -> {
                addExamItem()
            }
            is ExamEvent.GetAllExamItems -> {
                getAllExamItems()
            }
        }
    }

    init {
        getAllSubjects()
        getAllExamItems()
    }


    private fun getAllSubjects() = viewModelScope.launch {
        helper.getAllSubjectUseCaseResultHandler(
            onSuccess = {},
            onLoading = {},
            onError = {},
            data = { onEvent(ExamEvent.SetSubjectListState(it)) })
    }

    private fun getAllExamItems() = viewModelScope.launch {
        getAllExamItemsUseCase.invoke().collect() {
            when (it) {
                is Resource.Loading -> {
                    onEvent(ExamEvent.SetExamListState(list = it.data ?: listOf()))
                }
            }
        }
    }

    private fun addExamItem() = viewModelScope.launch {
        val exam = Exam(
            id = 0,
            title = titleState.value.text,
            description = descriptionState.value.text,
            subjectId = pickedSubjectState.value.id,
            date = dateState.value.time
        )
        addExamUseCase.invoke(exam = exam).collect() {
            when (it) {
                is Resource.Error -> {
                    it.data?.let { examResult ->
                        addOrEditError(eventResult = examResult)
                    }
                }
                is Resource.Loading -> { /* not needed */
                }
                is Resource.Success -> {
                    addOrEditSuccess()
                }
            }
        }
    }

    private fun addOrEditSuccess() {
        clearAfterExamEvent()
        removeAllErrors()

        onEvent(ExamEvent.ChangeBottomSheetState(false))
    }

    private fun addOrEditError(eventResult: ExamResult) {
        when (eventResult) {
            ExamResult.EmptyDescription -> _descriptionErrorState.value = true
            ExamResult.EmptyTitle -> _titleErrorState.value = true
            ExamResult.SubjectNotPicked -> onEvent(ExamEvent.SetPickedSubjectColorState(Color.Red))
            ExamResult.Success -> { /* no need */
            }
        }
    }

    private fun removeAllErrors() {
        _titleErrorState.value = false
        _descriptionErrorState.value = false
        _pickedSubjectColorState.value = Color.LightGray
    }

    private fun clearAfterExamEvent() {
        onEvent(ExamEvent.SetTitleState(text = ""))
        onEvent(ExamEvent.SetDescriptionState(text = ""))
        val subject = Subject(
            -1,
            "",
            0,
            "123",
            50.0,
            1.0,
            1.0
        )
        onEvent(ExamEvent.SetPickedSubjectState(subject))
        onEvent(ExamEvent.SetDateState(Calendar.getInstance().time))
    }

    fun getSubjectItem(examData: Exam): Subject {
        val errorSubject = Subject(-1, "", 0, "fds", 50.0, 50.0, 2.0)
        return if (subjectListState.value.listData.isNotEmpty()) {
            subjectListState.value.listData.filter { it.id == examData.subjectId }[0]
        } else {
            errorSubject
        }
    }

    fun isExamExpired(examLongDate: Long) : Boolean {
        val examDate = FormatDate.formatLongToDate(time = examLongDate)
        if(Calendar.getInstance().time.after(examDate)) {
            return true
        }
        return false
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
}