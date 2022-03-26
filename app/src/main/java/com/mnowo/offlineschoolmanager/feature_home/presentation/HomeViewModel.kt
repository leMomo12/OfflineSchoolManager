package com.mnowo.offlineschoolmanager.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.composesurveyapp.core.presentation.util.UiEvent
import com.mnowo.offlineschoolmanager.core.BottomBarNavigation
import com.mnowo.offlineschoolmanager.core.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onBottomEvent(bottomEvent: BottomBarNavigation) {
        when (bottomEvent) {
            is BottomBarNavigation.NavToTimetableScreen -> {
                bottomNav(Screen.TimetableScreen)
            }
            is BottomBarNavigation.NavToSubjectScreen -> {
                bottomNav(Screen.SubjectScreen)
            }
            is BottomBarNavigation.NavToToDoScreen -> {
                bottomNav(Screen.ToDoScreen)
            }
            is BottomBarNavigation.NavToExamScreen -> {
                bottomNav(Screen.ExamScreen)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {

        }
    }

     fun bottomNav(screen: Screen) {
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.Navigate(screen.route)
            )
        }
    }
}