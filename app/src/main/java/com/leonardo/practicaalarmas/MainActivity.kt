package com.leonardo.practicaalarmas

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        val btnStopAlarm = findViewById<Button>(R.id.btnStopAlarm)

        btnSetAlarm.setOnClickListener {
            setAlarm()
        }

        btnStopAlarm.setOnClickListener {
            stopAlarm()
        }

        // Programar una notificación después de cerrar la app
        scheduleNotification()
    }

    private fun setAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            action = "com.leonardo.practicaalarmas.ALARM_START"
        }

        pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = System.currentTimeMillis() + 5000 // Se activa en 5 segundos
        pendingIntent?.let {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, it)
        }
    }

    private fun stopAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            action = "com.leonardo.practicaalarmas.ALARM_START"
        }

        val stopPendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(stopPendingIntent) // Cancela la alarma

        // También envía un broadcast para detener la alarma
        val stopIntent = Intent(this, AlarmReceiver::class.java).apply {
            action = "com.leonardo.practicaalarmas.ALARM_STOP"
        }

        sendBroadcast(stopIntent)
    }

    private fun scheduleNotification() {
        val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}
