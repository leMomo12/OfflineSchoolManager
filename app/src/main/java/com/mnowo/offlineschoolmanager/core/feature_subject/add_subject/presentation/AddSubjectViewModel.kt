package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation

import android.content.Context
import android.content.res.loader.ResourcesProvider
import android.util.Log.d
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.TextFieldState
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Resource
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.color_picker.PickColorEvent
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.SubjectResult
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.use_case.AddSubjectUseCase
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.use_case.UpdateSubjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSubjectViewModel @Inject constructor(
    private val addSubjectUseCase: AddSubjectUseCase,
    private val updateSubjectUseCase: UpdateSubjectUseCase
) : ViewModel() {


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _bottomSheetState = mutableStateOf(true)
    val bottomSheetState: State<Boolean> = _bottomSheetState

    private val _subjectState = mutableStateOf(TextFieldState())
    val subjectState: State<TextFieldState> = _subjectState

    private val _roomState = mutableStateOf(TextFieldState())
    val roomState: State<TextFieldState> = _roomState

    private val _oralPercentageState = mutableStateOf(TextFieldState())
    val oralPercentageState: State<TextFieldState> = _oralPercentageState

    private val _writtenPercentageState = mutableStateOf(TextFieldState())
    val writtenPercentageState: State<TextFieldState> = _writtenPercentageState

    private val _colorState = mutableStateOf(value = Color.LightGray)
    val colorState: State<Color> = _colorState

    private val _subjectErrorState = mutableStateOf(false)
    val subjectErrorState: State<Boolean> = _subjectErrorState

    private val _oralErrorState = mutableStateOf(false)
    val oralErrorState: State<Boolean> = _oralErrorState

    private val _writtenErrorState = mutableStateOf(false)
    val writtenErrorState: State<Boolean> = _writtenErrorState

    private val _roomErrorState = mutableStateOf(false)
    val roomErrorState: State<Boolean> = _roomErrorState

    private val _showColorDialog = mutableStateOf(value = false)
    val showColorDialog: State<Boolean> = _showColorDialog

    private val _mustAddUpTo100ErrorState = mutableStateOf<Boolean>(false)
    val mustAddUpTo100ErrorState: State<Boolean> = _mustAddUpTo100ErrorState

    private val _specificSubjectState = mutableStateOf<Subject?>(null)
    val specificSubjectState: State<Subject?> = _specificSubjectState

    private val _editTextFieldState = mutableStateOf<Boolean>(false)
    val editTextFieldState: State<Boolean> = _editTextFieldState

    private val _editState = mutableStateOf<Boolean>(false)
    val editState: State<Boolean> = _editState

    fun setEditState(value: Boolean) {
        _editState.value = value
    }

    fun setEditTextFieldState(value: Boolean) {
        _editTextFieldState.value = value
    }

    fun setSpecificSubjectState(value: Subject?) {
        _specificSubjectState.value = value
    }

    fun setBottomSheetState(state: Boolean) {
        _bottomSheetState.value = state
    }

    fun onAddSubjectEvent(event: AddSubjectEvent) {
        when (event) {
            is AddSubjectEvent.EnteredSubject -> {
                _subjectState.value = subjectState.value.copy(
                    text = event.subject
                )
            }
            is AddSubjectEvent.EnteredRoom -> {
                _roomState.value = roomState.value.copy(
                    text = event.room
                )
            }
            is AddSubjectEvent.PickedColor -> {
                _showColorDialog.value = true
            }
            is AddSubjectEvent.AddSubject -> {
                viewModelScope.launch(Dispatchers.IO) {
                    removeAllErrors()
                    val color = colorState.value.toArgb()
                    if (oralPercentageState.value.text.trim()
                            .isBlank() || writtenPercentageState.value.text.trim().isBlank()
                    ) {
                        _oralErrorState.value = true
                        _writtenErrorState.value = true
                    } else {
                        val subject = Subject(
                            id = 0,
                            subjectName = subjectState.value.text,
                            color = color,
                            room = roomState.value.text,
                            oralPercentage = oralPercentageState.value.text.replace(
                                ',',
                                '.'
                            ).toDouble(),
                            writtenPercentage = writtenPercentageState.value.text.replace(
                                ',',
                                '.'
                            ).toDouble(),
                            average = 0.0
                        )
                        addSubjectUseCase.invoke(subject = subject).collect {
                            addAndUpdateUseCaseResult(result = it)
                        }
                    }
                }
            }
            is AddSubjectEvent.EditSubject -> {
                viewModelScope.launch(Dispatchers.IO) {
                    removeAllErrors()
                    val color = colorState.value.toArgb()
                    if (oralPercentageState.value.text.trim()
                            .isBlank() || writtenPercentageState.value.text.trim().isBlank()
                    ) {
                        _oralErrorState.value = true
                        _writtenErrorState.value = true
                    } else {
                        specificSubjectState.value?.id.let {
                            val subject = Subject(
                                id = specificSubjectState.value!!.id,
                                subjectName = subjectState.value.text,
                                color = color,
                                room = roomState.value.text,
                                oralPercentage = oralPercentageState.value.text.replace(
                                    ',',
                                    '.'
                                ).toDouble(),
                                writtenPercentage = writtenPercentageState.value.text.replace(
                                    ',',
                                    '.'
                                ).toDouble(),
                                average = 0.0
                            )

                            updateSubjectUseCase.invoke(subject = subject).collect() {
                                addAndUpdateUseCaseResult(result = it)
                            }
                        }
                    }
                }
            }

            is AddSubjectEvent.EnteredOralPercentage -> {
                _oralPercentageState.value = oralPercentageState.value.copy(
                    text = event.percentage
                )
            }
            is AddSubjectEvent.EnteredWrittenPercentage -> {
                _writtenPercentageState.value = writtenPercentageState.value.copy(
                    text = event.percentage
                )
            }
        }
    }

    // To handle add and edit useCase results
    private suspend fun addAndUpdateUseCaseResult(result: Resource<SubjectResult>) {
        when (result) {
            is Resource.Error -> {
                when (result.data) {
                    is SubjectResult.DoesntAddUpTo100 -> {
                        _oralErrorState.value = true
                        _writtenErrorState.value = true
                        _mustAddUpTo100ErrorState.value = true
                    }
                    is SubjectResult.ErrorOccurred -> {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                (result.data.message
                                    ?: R.string.unexpectedError) as String
                            )
                        )
                        clearAfterSubjectEvent()
                    }
                    is SubjectResult.EmptySubjectText -> {
                        _subjectErrorState.value = true
                    }
                    else -> {}
                }
            }
            is Resource.Success -> {
                viewModelScope.launch {
                    delay(300)
                    clearAfterSubjectEvent()
                    _bottomSheetState.value = false
                }
            }
            is Resource.Loading -> {}
        }
    }

    fun clearAfterSubjectEvent() {
        _subjectState.value.clearText()
        _colorState.value = Color.LightGray
        _roomState.value.clearText()
        _oralPercentageState.value.clearText()
        _writtenPercentageState.value.clearText()
    }

    fun removeAllErrors() {
        _subjectErrorState.value = false
        _writtenErrorState.value = false
        _oralErrorState.value = false
        _mustAddUpTo100ErrorState.value = false
    }

    fun onPickColorEvent(event: PickColorEvent) {
        when (event) {
            is PickColorEvent.DismissDialog -> {
                _showColorDialog.value = false
            }
            is PickColorEvent.ColorPicked -> {
                _colorState.value = event.color
            }
        }
    }
}