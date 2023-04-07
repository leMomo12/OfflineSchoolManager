package com.mnowo.offlineschoolmanager.core.feature_core.domain.use_case

import com.mnowo.offlineschoolmanager.feature_settings.domain.models.Settings
import com.mnowo.offlineschoolmanager.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    operator fun invoke() : Flow<Settings> = flow {
        val result = settingsRepository.getSettings()




    }
}