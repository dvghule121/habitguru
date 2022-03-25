package com.example.habitguru.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.habitguru.MainActivity
import com.example.habitguru.R
import com.example.habitguru.dataClasses.task
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
 * Use the [add_task.newInstance] factory method to
 * create an instance of this fragment.
 */
class add_task : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_add_task, container, false)
        val act = activity as MainActivity
        calendar = Calendar.getInstance()

        val title = view.findViewById<EditText>(R.id.task_name).text


        view.findViewById<ImageButton>(R.id.choose_time).setOnClickListener {
            showTimePicker()
        }

        view.findViewById<Button>(R.id.add_task_to_data).setOnClickListener {
            val mydb = DatabaseHandler(requireContext())
            val id = mydb.insertTask(task(0, title.toString(), calendar!!.time.toString(), 15, R.drawable.read, 0))
            Log.d(
                "TAG",
                "onCreateView: ${calendar!!.timeInMillis}. ${time!!.hours}, ${time!!.minutes}"
            )

            act.setAlarm(requireContext(),calendar!!, id,0)
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
         * @return A new instance of fragment add_task.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            add_task().apply {
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

        val currentYear: Int = calendar!!.get(Calendar.YEAR)
        val currentMonth: Int = calendar!!.get(Calendar.MONTH)
        val currentDay: Int = calendar!!.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { datePicker, yearOfDay, MonthOfDay, dayOfDay ->
                view?.findViewById<TextView>(R.id.time_choosen4)?.setText(
                    "${
                        String.format(
                            "%02d",
                            dayOfDay
                        )
                    }-${String.format("%02d", MonthOfDay + 1)}-$yearOfDay"
                )
                calendar!!.set(Calendar.YEAR, yearOfDay)
                calendar!!.set(Calendar.MONTH, MonthOfDay)
                calendar!!.set(Calendar.DATE, dayOfDay)
            },
            currentYear,
            currentMonth,
            currentDay,
        )
        datePickerDialog.show()

        picker!!.show(requireActivity().supportFragmentManager, "foxandroid")
        picker!!.addOnPositiveButtonClickListener(View.OnClickListener {

            view?.findViewById<TextView>(R.id.time_choosen)!!.text  = "${picker!!.hour}:${picker!!.minute}"
            time = Time(picker!!.hour,picker!!.minute,0)


            calendar!!.set(Calendar.HOUR_OF_DAY, picker!!.getHour())
            calendar!!.set(Calendar.MINUTE, picker!!.getMinute())
            calendar!!.set(Calendar.SECOND, 0)
            calendar!!.set(Calendar.MILLISECOND, 0)

        })


    }
}