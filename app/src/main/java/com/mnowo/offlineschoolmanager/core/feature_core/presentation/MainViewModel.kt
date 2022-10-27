package com.mnowo.offlineschoolmanager.core.feature_core.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStorePreferences: DataStore<Preferences>) :
    ViewModel() {

    fun incrementInAppCounter() = viewModelScope.launch {
        dataStorePreferences.edit { count ->
            val dataStoreKey = stringPreferencesKey(Constants.USER_IN_APP_COUNTER)
            val currentCounterValue = count[dataStoreKey]?.toInt()?.plus(1) ?: 0
            count[dataStoreKey] = currentCounterValue.toString()
        }
    }
}