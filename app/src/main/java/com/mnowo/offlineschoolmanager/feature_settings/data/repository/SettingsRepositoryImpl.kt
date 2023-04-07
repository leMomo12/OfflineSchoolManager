package com.mnowo.offlineschoolmanager.feature_settings.data.repository

import com.mnowo.offlineschoolmanager.feature_settings.data.local.SettingsDao
import com.mnowo.offlineschoolmanager.feature_settings.domain.models.Settings
import com.mnowo.offlineschoolmanager.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val settingsDao: SettingsDao) :
    SettingsRepository {

    override suspend fun setSettings(settings: Settings) {
        settingsDao.setSettings(settings)
    }

    override fun getSettings(): Settings {
        return settingsDao.getSettings()
    }
}