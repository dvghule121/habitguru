package com.example.habitguru.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitguru.MainActivity
import com.example.habitguru.R
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.dataClasses.health
import org.w3c.dom.Text

class Health_adapter (val context: Context): RecyclerView.Adapter<Health_adapterViewHolder>() {
    var health_list = emptyList<health>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Health_adapterViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.health_card, parent, false)
        return Health_adapterViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Health_adapterViewHolder, position: Int) {
        val health = health_list[position]
        holder.img.setImageResource(health.img)
        holder.task_progress.text = health.curProgress
        holder.total_progress.text = "${health.totalProgress} ${health.unit}"
        holder.task_progress_bar.max = health.totalProgress.toInt()
        holder.task_progress_bar.setProgress(health.curProgress.toInt())

        holder.img.setOnClickListener {
            val act = context as MainActivity
            val details = update_health_info()
            val bundle = Bundle()
            bundle.putString("name", health.title)
            details.arguments = bundle
            act.change(details)
        }
    }

    override fun getItemCount(): Int {
        return health_list.size
    }

    fun setData(habit_list: List<health>) {
        this.health_list = habit_list
        notifyDataSetChanged()
    }
}

class Health_adapterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    val img = itemView.findViewById<ImageView>(R.id.task_img)
    val task_progress = itemView.findViewById<TextView>(R.id.cur_progress)
    val task_progress_bar = itemView.findViewById<ProgressBar>(R.id.progressBar)
    val total_progress = itemView.findViewById<TextView>(R.id.task_detail)

}
