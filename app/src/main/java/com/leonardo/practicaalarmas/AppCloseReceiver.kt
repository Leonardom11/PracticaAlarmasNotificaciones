package com.leonardo.practicaalarmas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class AppCloseReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(10, TimeUnit.SECONDS) // Notificación tras 10 segundos
            .build()
        WorkManager.getInstance(context!!).enqueue(workRequest)
    }
}
