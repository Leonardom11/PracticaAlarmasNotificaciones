package com.leonardo.practicaalarmas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.leonardo.practicaalarmas.ALARM_START" -> startAlarm(context)
            "com.leonardo.practicaalarmas.ALARM_STOP" -> stopAlarm()
            Intent.ACTION_BOOT_COMPLETED -> restoreAlarms(context)
        }
    }

    private fun startAlarm(context: Context) {
        Log.d("AlarmReceiver", "¡Alarma activada!")

        if (mediaPlayer == null) {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, uri)
                isLooping = true
                prepare()
                start()
            }
        }
    }

    private fun stopAlarm() {
        Log.d("AlarmReceiver", "Deteniendo alarma...")

        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }

    private fun restoreAlarms(context: Context) {
        Log.d("AlarmReceiver", "Dispositivo reiniciado: restaurando alarmas...")
        // Aquí puedes reprogramar alarmas si es necesario
    }
}
