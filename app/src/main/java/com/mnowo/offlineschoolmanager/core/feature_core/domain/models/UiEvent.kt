package com.mnowo.offlineschoolmanager.core.feature_core.domain.models

sealed class UiEvent {

    data class ShowSnackbar(val uiText: String) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object NavigateUp : UiEvent()

}
