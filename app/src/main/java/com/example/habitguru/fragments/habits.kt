package com.example.habitguru.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.habitguru.R
import com.example.habitguru.adapter.MyAdapter
import com.example.habitguru.databaseHelper.DatabaseHandler
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [habits.newInstance] factory method to
 * create an instance of this fragment.
 */
class habits : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

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
        val view = inflater.inflate(R.layout.fragment_habits, container, false)

        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager = view.findViewById<ViewPager>(R.id.viewPager2)

        val mydb = DatabaseHandler(requireContext())
        val dateData = mydb.getAllTasksDate()


        if (dateData != null) {
            for(i in dateData){
                val date = i.get("DATE")
                val format1 = SimpleDateFormat("dd/M/yyyy")
                val dt1 = format1.parse(date)
                val format2: DateFormat = SimpleDateFormat("EEE")
                val finalDay = format2.format(dt1)
                Log.d("TAG", "onCreateView: $date")

                val date_view: View = inflater.inflate(R.layout.card_for_date,container, false)


                date_view.findViewById<TextView>(R.id.textView9).text = finalDay
                date_view.findViewById<TextView>(R.id.textView10).text = dt1.date.toString()
                tabLayout!!.addTab(tabLayout!!.newTab().setCustomView(date_view))

            }



            val adapter = MyAdapter(
                requireContext(),
                childFragmentManager,
                tabLayout!!.tabCount
            )
            viewPager!!.adapter = adapter


            viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager!!.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })
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
         * @return A new instance of fragment habits.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            habits().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getAllDateOfMonth(year: Int, month: Month): List<LocalDate> {
        val yearMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            YearMonth.of(year, month)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val firstDayOfTheMonth = yearMonth.atDay(1)
        val datesOfThisMonth = mutableListOf<LocalDate>()
        for (daysNo in LocalDate.now().dayOfMonth-4 until LocalDate.now().dayOfMonth) {

            datesOfThisMonth.add(firstDayOfTheMonth.plusDays(daysNo.toLong()))
        }
        return datesOfThisMonth
    }


}