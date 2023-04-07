package com.mnowo.offlineschoolmanager.feature_settings.domain.use_case

import com.mnowo.offlineschoolmanager.feature_settings.domain.models.Settings
import com.mnowo.offlineschoolmanager.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetSettingsUseCase @Inject constructor(private val repository: SettingsRepository) {

    suspend operator fun invoke(settings: Settings) = repository.setSettings(settings = settings)
}