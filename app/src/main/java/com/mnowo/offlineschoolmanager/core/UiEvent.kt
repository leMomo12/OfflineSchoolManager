package com.mnowo.composesurveyapp.core.presentation.util

sealed class UiEvent {

    data class ShowSnackbar(val uiText: String) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object NavigateUp : UiEvent()

}
