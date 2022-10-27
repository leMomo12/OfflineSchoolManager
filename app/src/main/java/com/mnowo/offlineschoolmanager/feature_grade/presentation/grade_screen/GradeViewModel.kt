package com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen

import android.app.Activity
import android.content.Context
import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.ListState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.ReviewService
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.GradeResult
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.*
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.RoundOffDecimals
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
    private val getAllGradesUseCase: GetAllGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase,
    private val updateAverageUseCase: UpdateAverageUseCase,
    private val getSpecificSubjectUseCase: GetSpecificSubjectUseCase,
    private val deleteSpecificGradeUseCase: DeleteSpecificGradeUseCase,
    private val updateGradeUseCase: UpdateGradeUseCase,
    private val reviewService: ReviewService,
    private val getInAppCounterUseCase: GetInAppCounterUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    //private val contexte = context

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

    private val _gradeState = mutableStateOf<Float>(0.75f)
    val gradeState: State<Float> = _gradeState

    private val _gradeErrorState = mutableStateOf(false)
    val gradeErrorState: State<Boolean> = _gradeErrorState

    private val _bottomSheetState = mutableStateOf(false)
    val bottomSheetState: State<Boolean> = _bottomSheetState

    private val _isWrittenState = mutableStateOf(true)
    val isWrittenState: State<Boolean> = _isWrittenState

    private val _subjectNameState = mutableStateOf("")
    val subjectNameState: State<String> = _subjectNameState

    private val _dropDownMenuState = mutableStateOf(false)
    val dropDownMenuState: State<Boolean> = _dropDownMenuState

    private val _editState = mutableStateOf(false)
    val editState: State<Boolean> = _editState

    private val _deleteState = mutableStateOf(false)
    val deleteState: State<Boolean> = _deleteState

    private val _deleteDialogState = mutableStateOf(false)
    val deleteDialogState: State<Boolean> = _deleteDialogState

    private val _deleteGradeIdState = mutableStateOf(-1)
    val deleteGradeIdState: State<Int> = _deleteGradeIdState

    private val _specificGradeState = mutableStateOf<Grade?>(null)
    val specificGradeState: State<Grade?> = _specificGradeState

    // Important for the TextFields in the GradeBottomSheet to get specific edit value
    private val _editTextFieldState = mutableStateOf<Boolean>(false)
    val editTextFieldState: State<Boolean> = _editTextFieldState

    private val _confettiDialogState = mutableStateOf(false)
    val confettiDialogState: State<Boolean> = _confettiDialogState

    fun setConfettiDialogState(value: Boolean) {
        _confettiDialogState.value = value
    }

    fun setEditTextFieldState(value: Boolean) {
        _editTextFieldState.value = value
    }

    fun setSpecificGradeState(value: Grade?) {
        _specificGradeState.value = value
    }

    fun setDeleteGradeIdState(value: Int) {
        _deleteGradeIdState.value = value
    }

    fun setDeleteDialogState(value: Boolean) {
        _deleteDialogState.value = value
    }

    fun setDeleteState(value: Boolean) {
        _deleteState.value = value
    }

    fun setEditState(value: Boolean) {
        _editState.value = value
    }

    fun setDropDownMenuState(value: Boolean) {
        _dropDownMenuState.value = value
    }

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
                val grade = Grade(
                    0,
                    subjectId = subjectId.value,
                    description = classTestDescriptionState.value.text,
                    grade = gradeState.value.toDouble(),
                    isWritten = isWrittenState.value
                )
                addGradeUseCase.invoke(grade = grade).onEach {
                    when (it) {
                        is Resource.Success -> {
                            addOrEditGradeSuccess()
                        }
                        is Resource.Error -> {
                            addOrEditGradeError(
                                errorMessage = it.message,
                                gradeResult = it.data
                            )
                        }
                    }

                }.launchIn(viewModelScope)
            }
            is GradeEvent.LoadGrades -> {
                viewModelScope.launch {
                    getAllGradesUseCase.invoke(subjectId = subjectId.value).collect() {
                        when (it) {
                            is Resource.Error -> {
                                it.message?.let { message ->
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            uiText = message
                                        )
                                    )
                                }
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
            is GradeEvent.GetSpecificSubject -> {
                viewModelScope.launch {
                    getSpecificSubjectUseCase.invoke(subjectId = subjectId.value).collect() {
                        _subjectNameState.value = it.subjectName
                    }
                }
            }
            is GradeEvent.EnteredClassTestDescription -> {
                _classTestDescriptionState.value = classTestDescriptionState.value.copy(
                    text = event.description
                )
            }
            is GradeEvent.EnteredGrade -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _gradeState.value =
                        RoundOffDecimals.roundOffDoubleDecimals(event.grade.toDouble()).toFloat()
                }
            }
            is GradeEvent.EnteredIsWritten -> {
                _isWrittenState.value = event.isWritten
            }
            is GradeEvent.DeleteSpecificGrade -> {
                viewModelScope.launch {
                    deleteSpecificGradeUseCase.invoke(gradeId = deleteGradeIdState.value)
                        .collect() {
                            when (it) {
                                is Resource.Success -> {
                                    updateAverageUseCase.invoke(subjectId = subjectId.value)
                                        .collect()
                                    _deleteDialogState.value = false
                                }
                                is Resource.Error -> {
                                    it.message?.let { string ->
                                        _eventFlow.emit(
                                            UiEvent.ShowSnackbar(
                                                uiText = string
                                            )
                                        )
                                    }
                                }
                            }
                        }
                }
            }
            is GradeEvent.UpdateGrade -> {
                viewModelScope.launch {
                    specificGradeState.value?.let {
                        updateGradeUseCase.invoke(
                            grade = Grade(
                                id = it.id,
                                subjectId = it.subjectId,
                                description = classTestDescriptionState.value.text,
                                grade = gradeState.value.toDouble(),
                                isWritten = isWrittenState.value,
                            )
                        ).collect() { result ->
                            when (result) {
                                is Resource.Success -> {
                                    addOrEditGradeSuccess()
                                }
                                is Resource.Loading -> {}
                                is Resource.Error -> {
                                    addOrEditGradeError(
                                        errorMessage = result.message,
                                        gradeResult = result.data
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is GradeEvent.NavBackToSubjectScreen -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.Navigate(
                            Screen.SubjectScreen.route
                        )
                    )
                }
            }
        }
    }

    // When adding or edit a grade failed
    private fun addOrEditGradeError(errorMessage: String?, gradeResult: GradeResult?) {
        viewModelScope.launch {
            when (gradeResult) {
                is GradeResult.EmptyDescription -> {
                    _classTestDescriptionErrorState.value = true
                }
                is GradeResult.GradeOutOffRange -> {
                    _gradeErrorState.value = true
                }
                is GradeResult.Exception -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            uiText = errorMessage ?: "Unexpected Error occurred"
                        )
                    )
                }
            }
        }
    }

    // When adding or edit a grade is successful
    private fun addOrEditGradeSuccess() {
        viewModelScope.launch {
            confettiAnimationLogic()
            clearAfterGradeEvent()
            removeAllErrors()
            _bottomSheetState.value = false

            updateAverageUseCase.invoke(subjectId = subjectId.value)
                .collect()
        }
    }

    private fun confettiAnimationLogic() {
        if (gradeState.value.toDouble() <= 1.75) {
            viewModelScope.launch {
                delay(200)
                setConfettiDialogState(value = true)
            }
            viewModelScope.launch {
                delay(5000)
                setConfettiDialogState(value = false)
                val inAppCounter = getInAppCounterUseCase.invoke()
                d("Counter", "Counter: $inAppCounter")
                delay(300)
                if (inAppCounter > 0) {
                    askForReview()
                }
            }
        }
    }

    fun clearAfterGradeEvent() {
        _classTestDescriptionState.value.clearText()
        _gradeState.value = 0.75f
        _isWrittenState.value = true
        setSpecificGradeState(null)
    }

    fun removeAllErrors() {
        _classTestDescriptionErrorState.value = false
        _gradeErrorState.value = false
    }

    private fun askForReview() {
        //reviewService.askForReview(context = contexte)
    }
}