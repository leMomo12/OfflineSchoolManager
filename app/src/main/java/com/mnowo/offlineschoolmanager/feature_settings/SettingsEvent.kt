package com.mnowo.offlineschoolmanager.feature_settings

sealed class SettingsEvent {
    object SetIsNormalGradeFormatState : SettingsEvent()
}
