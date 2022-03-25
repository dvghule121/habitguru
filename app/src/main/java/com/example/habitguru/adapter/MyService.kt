package com.example.habitguru.adapter

import android.R
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habitguru.Alarm_screen
import com.example.habitguru.databaseHelper.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*


class MyService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val context = this
        val mydb = DatabaseHandler(context)
        val type = intent!!.getIntExtra("type", 0)

        if ( type == 1) {
            val habit = mydb.getTasktById(intent?.getLongExtra("msg", -1))
            Log.d("TAG", "onStartCommand: ${intent?.getLongExtra("msg", -1)}")

            val intent = Intent(context, Alarm_screen::class.java)
            intent.putExtra("msg", habit.title)
            intent.putExtra("id", habit.id)
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date(habit.time))
            intent.putExtra("time",currentDate )
            intent.putExtra("icon", habit.img_id)
            intent.putExtra("type", type)


            context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, "foxandroid")
                    .setSmallIcon(R.drawable.ic_lock_idle_alarm)
                    .setContentTitle("Hurry!!, ${intent.getStringExtra("msg")} fast.")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
            builder.notification.flags = builder.notification.flags or Notification.FLAG_AUTO_CANCEL
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(123, builder.build())

        } else {
            val habit = mydb.getProjectById(intent?.getLongExtra("msg", -1))
            Log.d("TAG", "onStartCommand: ${intent?.getLongExtra("msg", -1)}")

            val intent = Intent(context, Alarm_screen::class.java)
            intent.putExtra("msg", habit.title)
            intent.putExtra("time", habit.time.toString())
            intent.putExtra("icon", habit.img_id)


            context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, "foxandroid")
                    .setSmallIcon(R.drawable.ic_lock_idle_alarm)
                    .setContentTitle("Hurry!!, ${intent.getStringExtra("msg")} fast.")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
            builder.notification.flags = builder.notification.flags or Notification.FLAG_AUTO_CANCEL
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(123, builder.build())
        }







        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }
}