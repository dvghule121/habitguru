package com.example.habitguru.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitguru.MainActivity
import com.example.habitguru.R
import com.example.habitguru.adapter.HabitsAdapter
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.databaseHelper.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [habit_page.newInstance] factory method to
 * create an instance of this fragment.
 */
class habit_page : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_habit_page, container, false)
        val habit_view = view.findViewById<RecyclerView>(R.id.habit_view)
        habit_view.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        val bundle = arguments
        if (bundle != null){
            val date = bundle.getString("date")
            Log.d("TAG", "onCreateView: $date")
            val adapter = HabitsAdapter(requireContext(), date)

            habit_view.adapter = adapter
            val mydb = DatabaseHandler(requireContext())
            val habit_list = ArrayList<habit>()
            mydb.getAllHabits()?.let { habit_list.addAll(it) }

            if (habit_list.isEmpty()){
                view.findViewById<ImageView>(R.id.hint_img).setImageResource(R.drawable.study)
                view.findViewById<ImageView>(R.id.hint_img).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.hint).visibility = View.VISIBLE
            }

            adapter.setData(habit_list)
        }



        view.findViewById<FloatingActionButton>(R.id.add_habbit).setOnClickListener {
            val act = activity as MainActivity
            act.change(add_habit())
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
         * @return A new instance of fragment habit_page.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            habit_page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}