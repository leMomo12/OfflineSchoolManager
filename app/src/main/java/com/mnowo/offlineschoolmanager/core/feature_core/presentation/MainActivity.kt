package com.mnowo.offlineschoolmanager.core.feature_core.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.mnowo.offlineschoolmanager.core.Navigation
import com.mnowo.offlineschoolmanager.core.theme.OfflineSchoolManagerTheme
import com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util.RoundOffDecimals
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfflineSchoolManagerTheme {
                val navController = rememberNavController()
                Navigation(navController = navController)

                val viewModel: MainViewModel = hiltViewModel()

                LaunchedEffect(key1 = true) {
                    viewModel.incrementInAppCounter()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Exam reminder"
            val description = "Exam reminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel("notifyStudent", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}