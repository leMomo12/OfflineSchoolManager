package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.use_case.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.mnowo.offlineschoolmanager.R
import javax.inject.Inject

class ExamNotificationReceiver @Inject constructor(

) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val builder = NotificationCompat.Builder(it, "notifyStudent")
                .setSmallIcon(R.drawable.assigement_icon)
                .setContentTitle("Hey student, your exam is Expired")
                .setContentText("Add a result!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager = ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.notify(200, builder.build())
        }
    }
}