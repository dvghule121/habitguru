package com.example.habitguru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.habitguru.R
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.dataClasses.task
import com.example.habitguru.databaseHelper.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(val context: Context) : RecyclerView.Adapter<TaskAdapterViewHolder>() {

    var habit_list = emptyList<task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapterViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.habit_card, parent, false)
        return TaskAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskAdapterViewHolder, position: Int) {
        val habit = habit_list[position]
        val mydb = DatabaseHandler(context)
        val sdf = SimpleDateFormat("dd-M-yyyy   hh:mm")
        val currentDate = sdf.format(Date(habit.time))

        holder.title.text = habit.title
        holder.time.text = currentDate
        holder.image.setImageResource(habit.img_id)




        if (habit.status == 1) {
            holder.project_task.isChecked = true
            holder.project_task.isEnabled = false
        }
        holder.delete.setOnClickListener {
            mydb.deleteTask(habit.id)

        }
        holder.project_task.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show()
                habit.status = 1
                mydb.updateTask(habit.id, habit)
            }
        }
    }

    override fun getItemCount(): Int {
        return habit_list.size
    }

    fun setData(habit_list: List<task>) {
        this.habit_list = habit_list
        notifyDataSetChanged()
    }
}

class TaskAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image = itemView.findViewById<ImageView>(R.id.habit_icon)
    val title = itemView.findViewById<TextView>(R.id.habit_title)
    val time = itemView.findViewById<TextView>(R.id.habit_time)
    val project_task = itemView.findViewById<CheckBox>(R.id.checkBox)
    val delete = itemView.findViewById<ImageButton>(R.id.delete_habit)
}