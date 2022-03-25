package com.example.habitguru.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitguru.R
import com.example.habitguru.adapter.Health_adapter
import com.example.habitguru.databaseHelper.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [health.newInstance] factory method to
 * create an instance of this fragment.
 */
class health : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_health, container, false)
        val health_view = view.findViewById<RecyclerView>(R.id.health_view)
        val adapter = Health_adapter(requireContext())
        health_view.adapter = adapter
        health_view.layoutManager = GridLayoutManager(requireContext(),2)
        val mydb= DatabaseHandler(requireContext())
        val health_cards = mydb.get_health()
        val vals = mydb.getAllTasksDate_Dashboard()

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())


        if (vals != null) {
            for(i in vals){
                for (j in health_cards){
                    j.curProgress = i.get(j.title)!!
                }
            }
        }
        adapter.setData(health_cards)



        return  view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment health.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            health().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}