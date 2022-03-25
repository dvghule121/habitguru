package com.example.habitguru.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitguru.R
import com.example.habitguru.adapter.HabitsAdapter
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.databaseHelper.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [dashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class dashboard : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_dashboard, container, false)


        val habit_view = view.findViewById<RecyclerView>(R.id.habit_view)
        habit_view.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        val adapter = HabitsAdapter(requireContext(), currentDate)

        habit_view.adapter = adapter
        val mydb = DatabaseHandler(requireContext())
        val habit_list = ArrayList<habit>()
        mydb.getAllHabits()?.let { habit_list.addAll(it) }
        val habit_list_current = ArrayList<habit>()
        for (i in habit_list){
            if (i.time.hours in Calendar.getInstance().time.hours-1..Calendar.getInstance().time.hours+1 ){
                habit_list_current.add(i)

            }
        }

        if (habit_list_current.size == 0){
            view.findViewById<ImageView>(R.id.hint_img).setImageResource(R.drawable.read)
            view.findViewById<ImageView>(R.id.hint_img).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.hint).visibility = View.VISIBLE
        }

        adapter.setData(habit_list_current)
        val target_list = mydb.Target_Dashboard()
        val vals = mydb.getAllTasksDate_Dashboard()

        var completed = 0
        for (i in mydb.getAllTasks()!!){
            if (i.status == 1){
                completed += 1
            }
        }

        try {
            val health = mydb.get_health_by_name("WATER")
            val read = mydb.get_health_by_name("READ")
            val weight = mydb.get_health_by_name("WEIGHT")

            view.findViewById<TextView>(R.id.glasses_consumed).text = vals!!.get(0)["WATER"] + " Glasses"
            view.findViewById<TextView>(R.id.drink_water_progress).text = vals.get(0).get("WATER") + " / " + health.totalProgress+" " +  health.unit

            view.findViewById<TextView>(R.id.chapter_read).text = vals.get(0).get("READ") + " chapters"
            view.findViewById<TextView>(R.id.reading_progress).text = vals.get(0).get("READ") + " / " + read.totalProgress+" " +  read.unit

            view.findViewById<TextView>(R.id.task_completed).text = completed.toString() + "   completed"
            view.findViewById<TextView>(R.id.task_progress).text = completed.toString() + " / " + mydb.getAllTasks()!!.size.toString() + " tasks"

            view.findViewById<TextView>(R.id.cur_weight).text = vals.get(0).get("WEIGHT") + " Kgs"
            view.findViewById<TextView>(R.id.target_weight).text = vals.get(0).get("WEIGHT") + " / " + weight.totalProgress +" " +  weight.unit

        }
        catch (e:Exception){
            true
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dashboard.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            dashboard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}