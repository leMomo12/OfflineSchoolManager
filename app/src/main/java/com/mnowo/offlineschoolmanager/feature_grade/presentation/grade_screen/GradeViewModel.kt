package com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen

import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.AddSubjectEvent
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.AddGradeResult
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.AddGradeUseCase
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetAllGradesUseCase
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.GetSpecificSubjectUseCase
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.UpdateAverageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
    private val getAllGradesUseCase: GetAllGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase,
    private val updateAverageUseCase: UpdateAverageUseCase,
    savedStateHandle: SavedStateHandle,
    private val repository: GradeRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _gradeListState = mutableStateOf<ListState<Grade>>(ListState())
    val gradeListState: State<ListState<Grade>> = _gradeListState

    private val _subjectId = mutableStateOf<Int>(-1)
    val subjectId: State<Int> = _subjectId

    private val _classTestDescriptionState = mutableStateOf(TextFieldState())
    val classTestDescriptionState: State<TextFieldState> = _classTestDescriptionState

    private val _classTestDescriptionErrorState = mutableStateOf(false)
    val classTestDescriptionErrorState: State<Boolean> = _classTestDescriptionErrorState

    private val _gradeState = mutableStateOf<TextFieldState>(TextFieldState())
    val gradeState: State<TextFieldState> = _gradeState

    private val _gradeErrorState = mutableStateOf<Boolean>(false)
    val gradeErrorState: State<Boolean> = _gradeErrorState

    private val _bottomSheetState = mutableStateOf<Boolean>(false)
    val bottomSheetState: State<Boolean> = _bottomSheetState

    private val _isWrittenState = mutableStateOf<Boolean>(true)
    val isWrittenState: State<Boolean> = _isWrittenState

    fun setBottomSheetState(value: Boolean) {
        _bottomSheetState.value = value
    }

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
            is GradeEvent.AddGrade -> {
                if (gradeState.value.text.trim().isNotBlank()) {
                    val grade = Grade(
                        0,
                        subjectId = subjectId.value,
                        description = classTestDescriptionState.value.text,
                        grade = gradeState.value.text.toDouble(),
                        isWritten = isWrittenState.value
                    )
                    addGradeUseCase.invoke(grade = grade).onEach { it ->
                        when (it) {
                            is Resource.Success -> {
                                viewModelScope.launch {
                                    clearAfterAddGradeEvent()
                                    removeAllErrors()
                                    _bottomSheetState.value = false

                                    updateAverageUseCase.invoke(subjectId = subjectId.value)
                                        .collect() {

                                        }
                                }
                            }
                            is Resource.Error -> {
                                viewModelScope.launch {
                                    when (it.data) {
                                        is AddGradeResult.EmptyDescription -> {
                                            _classTestDescriptionErrorState.value = true
                                        }
                                        is AddGradeResult.GradeOutOffRange -> {
                                            _gradeErrorState.value = true
                                        }
                                        is AddGradeResult.Exception -> {
                                            _eventFlow.emit(
                                                UiEvent.ShowSnackbar(
                                                    uiText = it.message.toString()
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            is Resource.Loading -> {

                            }
                        }

                    }.launchIn(viewModelScope)
                } else {
                    _gradeErrorState.value = true
                }
            }
            is GradeEvent.LoadGrades -> {
                viewModelScope.launch {
                    getAllGradesUseCase.invoke(subjectId = subjectId.value).collect() {
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
                    }
                }
            }
            is GradeEvent.EnteredClassTestDescription -> {
                _classTestDescriptionState.value = classTestDescriptionState.value.copy(
                    text = event.description
                )
            }
            is GradeEvent.EnteredGrade -> {
                _gradeState.value = gradeState.value.copy(
                    text = event.grade
                )
            }
            is GradeEvent.EnteredIsWritten -> {
                _isWrittenState.value = event.isWritten
            }
        }
    }

    private fun clearAfterAddGradeEvent() {
        _classTestDescriptionState.value.clearText()
        _gradeState.value.clearText()
        _isWrittenState.value = true
    }

    private fun removeAllErrors() {
        _classTestDescriptionErrorState.value = false
        _gradeErrorState.value = false
    }
}