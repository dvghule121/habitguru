package com.example.habitguru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.habitguru.R
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.databaseHelper.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*

class HabitsAdapter(val context: Context, val date: String?) : RecyclerView.Adapter<HabitsAdapterViewHolder>() {

    var habit_list = emptyList<habit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsAdapterViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.habit_card, parent, false)
        return HabitsAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitsAdapterViewHolder, position: Int) {
        val habit = habit_list[position]
        val mydb = DatabaseHandler(context)

        holder.title.text = habit.title
        holder.time.text = String.format("%02d", habit.time.hours) + ":" + String.format(
            "%02d",
            habit.time.minutes
        )
        holder.image.setImageResource(habit.img_id)
        val task = mydb.getTasksDateByTask(habit.title.uppercase(), date!!)
            ?.get(habit.title.uppercase())?.toInt()








        if (task == 1) {
            holder.project_task.isChecked = true
            holder.project_task.isEnabled = false
        }
        holder.delete.setOnClickListener {
            mydb.deleteTask(habit.id)

        }
        holder.project_task.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show()
                mydb.updateDate(date, habit.title.uppercase())
            }
        }
    }

    override fun getItemCount(): Int {
        return habit_list.size
    }

    fun setData(habit_list: List<habit>) {
        this.habit_list = habit_list
        notifyDataSetChanged()
    }
}

class HabitsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image = itemView.findViewById<ImageView>(R.id.habit_icon)
    val title = itemView.findViewById<TextView>(R.id.habit_title)
    val time = itemView.findViewById<TextView>(R.id.habit_time)
    val project_task = itemView.findViewById<CheckBox>(R.id.checkBox)
    val delete = itemView.findViewById<ImageButton>(R.id.delete_habit)
}
