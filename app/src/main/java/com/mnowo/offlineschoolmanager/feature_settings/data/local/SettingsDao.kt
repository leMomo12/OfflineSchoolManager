package com.mnowo.offlineschoolmanager.feature_settings.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mnowo.offlineschoolmanager.feature_settings.domain.models.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSettings(settings: Settings)

    @Query("SELECT * FROM settings_table")
    fun getSettings() : Settings
}