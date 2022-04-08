package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.color_picker.PickColorEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _subjectListState = mutableStateOf<ListState<Subject>>(ListState())
    val subjectListState: State<ListState<Subject>> = _subjectListState

    init {
        getAllSubjectsUseCase.invoke().onEach {
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
        }.launchIn(viewModelScope)
    }


    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnSubjectClicked -> {

            }
            is SubjectEvent.AddSubject -> {

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
            if(screen != currentScreen) {
                _eventFlow.emit(
                    UiEvent.Navigate(screen.route)
                )
            }
        }
    }
}
