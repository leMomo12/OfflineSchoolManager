package com.mnowo.offlineschoolmanager.core.feature_core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mnowo.offlineschoolmanager.core.Navigation
import com.mnowo.offlineschoolmanager.core.theme.OfflineSchoolManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfflineSchoolManagerTheme {
                val navController = rememberNavController()
                Navigation(navController = navController)
            }
        }
    }
}