package com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen

import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllGradesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
    private val getAllGradesUseCase: GetAllGradesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _gradeListState = mutableStateOf<ListState<Grade>>(ListState())
    val gradeListState: State<ListState<Grade>> = _gradeListState

    private val _subjectId = mutableStateOf<Int>(-1)
    val subjectId: State<Int> = _subjectId

    fun setSubjectId(value: Int) {
        _subjectId.value = value
    }


    fun onEvent(event: GradeEvent) {
        when (event) {
            is GradeEvent.GradeListData -> {
                _gradeListState.value = gradeListState.value.copy(
                    listData = event.listData
                )
            }
            is GradeEvent.LoadGrades -> {
                getAllGradesUseCase.invoke(subjectId = subjectId.value).onEach {
                    when (it) {
                        is Resource.Error -> {

                        }
                        is Resource.Success -> {

                        }
                        is Resource.Loading -> {
                            it.data?.let { item ->
                                onEvent(GradeEvent.GradeListData(item))
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

}