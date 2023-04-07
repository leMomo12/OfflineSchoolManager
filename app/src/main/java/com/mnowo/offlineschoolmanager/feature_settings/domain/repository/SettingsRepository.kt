package com.mnowo.offlineschoolmanager.feature_settings.domain.repository

import com.mnowo.offlineschoolmanager.feature_settings.domain.models.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setSettings(settings: Settings)

    fun getSettings() : Settings
}