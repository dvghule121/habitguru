package com.example.habitguru
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.habitguru.adapter.AlarmReceiver
import com.example.habitguru.adapter.update_health_info
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.dataClasses.task
import com.example.habitguru.databaseHelper.DatabaseHandler
import com.example.habitguru.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null
    private var picker: MaterialTimePicker? = null
    var calendar :Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        val mydb = DatabaseHandler(this)
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        mydb.insertDate(currentDate)





        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> change(dashboard())
                    R.id.health -> change(health())
                    R.id.habits -> change(habits())
                    R.id.todo -> change(task_page())

                }

            true
        }

        change(dashboard())
    }

    fun change(toFragment: Fragment) {
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        val transaction: FragmentTransaction =
            manager.beginTransaction() //create an instance of Fragment-transaction
        transaction.replace(R.id.main_view, toFragment, "Frag_Top_tag")
        transaction.commit()
    }

    fun setAlarm(context: Context, calendar: Calendar, msg: Long) {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("msg", msg)
        intent.putExtra("type", 0)
        pendingIntent = PendingIntent.getBroadcast(context, msg.toInt(), intent, 0)
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
        Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }

    fun setAlarm(context: Context, calendar: Calendar, msg: Long, a:Int) {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("msg", msg)
        intent.putExtra("type", 1)
        pendingIntent = PendingIntent.getBroadcast(context, msg.toInt(), intent, 0)
        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            pendingIntent
        )
        Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(id:Int) {
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0)
        if (alarmManager == null) {
            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        }
        alarmManager!!.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()

    }

    fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()
        picker!!.show(supportFragmentManager, "foxandroid")
        picker!!.addOnPositiveButtonClickListener(View.OnClickListener {
            calendar = Calendar.getInstance()
            calendar!!.set(Calendar.HOUR_OF_DAY, picker!!.getHour())
            calendar!!.set(Calendar.MINUTE, picker!!.getMinute())
            calendar!!.set(Calendar.SECOND, 0)
            calendar!!.set(Calendar.MILLISECOND, 0)
        })

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "foxandroidReminderChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("foxandroid", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }


}