package com.mnowo.offlineschoolmanager.core.feature_core.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.review.ReviewManagerFactory
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
            }
        }
    }

    private fun askForReview() {
        val manager = ReviewManagerFactory.create(this)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                manager.launchReviewFlow(this, reviewInfo).addOnFailureListener {
                }.addOnCompleteListener { _ ->
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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