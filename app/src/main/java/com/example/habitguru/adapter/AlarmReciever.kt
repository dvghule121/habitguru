package com.example.habitguru.adapter

import android.app.Service.START_STICKY
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.habitguru.Alarm_screen
import com.example.habitguru.databaseHelper.DatabaseHandler


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val new_intent = Intent(context, MyService::class.java)
        new_intent.putExtra("msg",intent.getLongExtra("msg",0))
        new_intent.putExtra("type",intent.getIntExtra("type",0))
        context.startService(new_intent)

    }
}