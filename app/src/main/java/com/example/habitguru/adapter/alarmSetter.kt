package com.example.habitguru.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.habitguru.MainActivity
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.*


class alarmSetter {
    //            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
//            intent.putExtra(AlarmClock.EXTRA_HOUR, time!!.hours)
//            intent.putExtra(AlarmClock.EXTRA_MINUTES, time!!.minutes)
//            intent.putExtra(AlarmClock.EXTRA_MESSAGE,title)
//            startActivity(intent)

    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null

    fun setAlarm(context: Context, calendar: Calendar) {
        alarmManager = getSystemService(context, ALARM_SERVICE.javaClass) as AlarmManager?
        val intent = Intent(context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
        Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }



}

