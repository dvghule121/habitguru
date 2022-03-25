package com.example.habitguru

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.habitguru.adapter.AlarmReceiver
import com.example.habitguru.databaseHelper.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*


class Alarm_screen : AppCompatActivity() {
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWhenLockedAndTurnScreenOn()

        getActionBar()?.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_alarm_screen)
        // initialise the layout
        val webView = findViewById<ImageView>(R.id.imageView4);
        val intent = intent
        val task_name = intent.getStringExtra("msg")
        val task_id = intent.getIntExtra("id",-1)
        val task_type = intent.getIntExtra("type", 0)
        val task_time = intent.getStringExtra("time")
        val task_icon = intent.getIntExtra("icon", R.drawable.c193504b9bc094cd843d5af1d54dcfc5)
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        // Adding the gif h ere using glide library
        Glide.with(this).load(task_icon).into(webView);
        findViewById<TextView>(R.id.task_name).text = task_name
        findViewById<TextView>(R.id.task_alarm_time).text = task_time


        // add the url of gif
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val r = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()
        findViewById<Button>(R.id.button).setOnClickListener {

            r.stop()

            val mydb = DatabaseHandler(this)

            if (task_type == 1) {
                Log.d("TAG", "taskId: $task_id")
                val task = mydb.getTasktById(task_id?.toLong())

                task.status = 1
                mydb.updateTask(task.id, task)

            } else {
                if (task_name != null) {
                    mydb.updateDate(currentDate, task_name)
                }

            }
            this.finish()
        }
    }

    private fun cancelAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        if (alarmManager == null) {
            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        }
        alarmManager!!.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()

    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }
}