package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.composesurveyapp.core.presentation.util.UiEvent
import com.mnowo.offlineschoolmanager.core.AddSubjectEvent
import com.mnowo.offlineschoolmanager.core.Screen
import com.mnowo.offlineschoolmanager.core.TextFieldState
import com.mnowo.offlineschoolmanager.feature_home.presentation.HomeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(

) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private val _subjectState = mutableStateOf(TextFieldState())
    val subjectState: State<TextFieldState> = _subjectState

    private val _roomState = mutableStateOf(TextFieldState())
    val roomState: State<TextFieldState> = _roomState

    private val _colorState = mutableStateOf<Color?>(null)
    val colorState: State<Color?> = _colorState

    private val _subjectErrorState = mutableStateOf<Boolean>(false)
    val subjectErrorState: State<Boolean> = _subjectErrorState

    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnSubjectClicked -> {

            }
            is SubjectEvent.AddSubject -> {

            }
            is SubjectEvent.More -> {

            }
        }
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
                _colorState.value = event.color
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