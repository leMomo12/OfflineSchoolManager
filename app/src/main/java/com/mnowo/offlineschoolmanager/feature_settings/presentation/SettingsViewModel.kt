package com.mnowo.offlineschoolmanager.feature_settings.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.feature_settings.domain.models.Settings
import com.mnowo.offlineschoolmanager.feature_settings.domain.use_case.SetSettingsUseCase
import com.mnowo.offlineschoolmanager.feature_settings.presentation.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val setSettingsUseCase: SetSettingsUseCase) :
    ViewModel() {

    private val _isNormalGradeFormatState = mutableStateOf<Boolean>(false)
    val isNormalGradeFormatState: State<Boolean> = _isNormalGradeFormatState

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetIsNormalGradeFormatState -> {
                _isNormalGradeFormatState.value = !isNormalGradeFormatState.value
            }
        }
        setSettings()
    }

    private fun setSettings() = viewModelScope.launch {
        val settings = Settings(1, isNormalGradeFormatState.value)

        setSettingsUseCase.invoke(settings = settings)
    }
}