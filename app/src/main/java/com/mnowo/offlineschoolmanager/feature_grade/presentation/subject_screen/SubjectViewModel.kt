package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Helper
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.DeleteSubjectUseCase
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
    private val helper: Helper
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _subjectListState = mutableStateOf<ListState<Subject>>(ListState())
    val subjectListState: State<ListState<Subject>> = _subjectListState

    private val _onSubjectListClickedIndexState = mutableStateOf<Int>(-1)
    val onSubjectListClickedIndexState: State<Int> = _onSubjectListClickedIndexState

    private val _dropDownMenuState = mutableStateOf<Boolean>(false)
    val dropDownMenuState: State<Boolean> = _dropDownMenuState

    fun setDropDownMenuState(value: Boolean) {
        _dropDownMenuState.value = value
    }

    private val _editState = mutableStateOf<Boolean>(false)
    val editState: State<Boolean> = _editState

    fun setEditState(value: Boolean) {
        _editState.value = value
    }

    private val _deleteState = mutableStateOf<Boolean>(false)
    val deleteState: State<Boolean> = _deleteState

    fun setDeleteState(value: Boolean) {
        _deleteState.value = value
    }

    private val _deleteDialogState = mutableStateOf<Boolean>(false)
    val deleteDialogState: State<Boolean> = _deleteDialogState

    fun setDeleteDialogState(value: Boolean) {
        _deleteDialogState.value = value
    }

    private val _subjectIdState = mutableStateOf<Int>(-1)
    val subjectIdState: State<Int> = _subjectIdState

    fun setSubjectIdState(value: Int) {
        _subjectIdState.value = value
    }

    init {
        getAllSubjects()
    }

    fun getAllSubjects() = viewModelScope.launch {
        helper.getAllSubjectUseCaseResultHandler(
            onSuccess = {},
            onLoading = {},
            onError = {},
            data = { onEvent(SubjectEvent.SubjectListData(listData = it)) })
    }

fun onEvent(event: SubjectEvent) {
    when (event) {
        is SubjectEvent.OnSubjectClicked -> {
            viewModelScope.launch {
                _onSubjectListClickedIndexState.value = event.id
                _eventFlow.emit(
                    UiEvent.Navigate(Screen.GradeScreen.route)
                )
            }
        }
        is SubjectEvent.SubjectListData -> {
            _subjectListState.value = subjectListState.value.copy(
                listData = event.listData
            )
        }
        is SubjectEvent.DeleteSubject -> {
            viewModelScope.launch {
                deleteSubjectUseCase.invoke(subjectId = subjectIdState.value).collect()
            }
            setDeleteDialogState(false)
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
}
