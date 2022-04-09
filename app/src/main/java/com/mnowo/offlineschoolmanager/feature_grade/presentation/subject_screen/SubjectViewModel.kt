package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.color_picker.PickColorEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen.GradeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase,
    savedStateHandle: SavedStateHandle,
    private val gradeRepository: GradeRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _subjectListState = mutableStateOf<ListState<Subject>>(ListState())
    val subjectListState: State<ListState<Subject>> = _subjectListState

    private val _onSubjectListClickedIndexState = mutableStateOf<Int>(-1)
    val onSubjectListClickedIndexState: State<Int> = _onSubjectListClickedIndexState

    init {
        getAllSubjects()
    }

    fun getAllSubjects() {
        viewModelScope.launch {
            getAllSubjectsUseCase.invoke().collect() {
                when (it) {
                    is Resource.Success -> {
                    }
                    is Resource.Loading -> {
                        it.data?.let { result ->
                            onEvent(SubjectEvent.SubjectListData(result))
                        }
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
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
            is SubjectEvent.More -> {

            }
            is SubjectEvent.SubjectListData -> {
                _subjectListState.value = subjectListState.value.copy(
                    listData = event.listData
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
}
