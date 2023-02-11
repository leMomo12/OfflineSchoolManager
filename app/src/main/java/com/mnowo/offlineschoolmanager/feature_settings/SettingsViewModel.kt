package com.mnowo.offlineschoolmanager.feature_settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _isNormalGradeFormatState = mutableStateOf<Boolean>(false)
    val isNormalGradeFormatState: State<Boolean> = _isNormalGradeFormatState

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.SetIsNormalGradeFormatState -> {
                _isNormalGradeFormatState.value = !isNormalGradeFormatState.value
            }
        }
    }
}