package com.example.habitguru.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.habitguru.MainActivity
import com.example.habitguru.R
import com.example.habitguru.dataClasses.Image_id
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.databaseHelper.DatabaseHandler
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.sql.Time
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [add_habit.newInstance] factory method to
 * create an instance of this fragment.
 */
class add_habit : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var picker: MaterialTimePicker? = null
    private var calendar :Calendar? = null
    private var time : Time? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_habit, container, false)
        var img_icon:Int? = null
        val act = activity as MainActivity
        val array = resources.getStringArray(R.array.habits)
        var title = ""
        val spinner = view.findViewById<Spinner>(R.id.spinner)


        view.findViewById<ImageButton>(R.id.choose_time).setOnClickListener {
            showTimePicker()
        }



        if (spinner != null) {

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    title = array[position]
                    img_icon = Image_id().image_list[position]
                    Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }





        view.findViewById<Button>(R.id.add_habit).setOnClickListener {
            val mydb = DatabaseHandler(requireContext())
            val habits = mydb.getAllHabits()
            var id = -1
            for(i in habits!!){
                if(i.title == title){
                    id = i.id
                }
            }
            val habit_to_add = habit(id, title, time!!, 15, img_icon!!, 0)
            if (id == -1){
                id = mydb.insertHabit(habit_to_add).toInt()
            }else{
                mydb.updateHabit(id,habit_to_add )
                act.cancelAlarm(id)
            }

            Log.d(
                "TAG",
                "onCreateView: ${calendar!!.timeInMillis}. ${time!!.hours}, ${time!!.minutes}"
            )

            act.setAlarm(requireContext(),calendar!!, id.toLong())
            Log.d("TAG", "onCreateView: $id")
            act.change(habits())


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
         * @return A new instance of fragment add_habit.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            add_habit().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()
        picker!!.show(requireActivity().supportFragmentManager, "foxandroid")
        picker!!.addOnPositiveButtonClickListener(View.OnClickListener {

            view?.findViewById<TextView>(R.id.time_choosen)!!.text  = "${picker!!.hour}:${picker!!.minute}"
            time = Time(picker!!.hour,picker!!.minute,0)

            calendar = Calendar.getInstance()
            calendar!!.set(Calendar.HOUR_OF_DAY, picker!!.getHour())
            calendar!!.set(Calendar.MINUTE, picker!!.getMinute())
            calendar!!.set(Calendar.SECOND, 0)
            calendar!!.set(Calendar.MILLISECOND, 0)

        })


    }





}