package com.example.habitguru.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.habitguru.MainActivity
import com.example.habitguru.R
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.dataClasses.health
import com.example.habitguru.databaseHelper.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [update_health_info.newInstance] factory method to
 * create an instance of this fragment.
 */
class update_health_info : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var health: health

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update_health_info, container, false)
        var curProgress = 0
        val mydb = DatabaseHandler(requireContext())
        val vals = mydb.getAllTasksDate_Dashboard()!!.get(0)


        val builder = arguments
        if (builder != null){



            curProgress = getCurrentProgress(builder.get("name").toString())

            health = DatabaseHandler(requireContext()).get_health_by_name(builder.get("name").toString())
            view.findViewById<TextView>(R.id.title_text).text = health.title
            view.findViewById<TextView>(R.id.target).text = health.totalProgress
            view.findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
                health.totalProgress = (health.totalProgress.toInt() + 1).toString()
                view.findViewById<TextView>(R.id.target).text = health.totalProgress
                view.findViewById<ProgressBar>(R.id.progressBarHealth).max = health.totalProgress.toInt()
                view.findViewById<TextView>(R.id.textView7).text = "$curProgress / ${health.totalProgress}"
                mydb.updateHealth(builder?.get("name").toString(),health.totalProgress.toInt())
            }

            view.findViewById<TextView>(R.id.cur).text = curProgress.toString()
            view.findViewById<ImageView>(R.id.health_image).setImageResource(health.img)
            view.findViewById<ProgressBar>(R.id.progressBarHealth).max = health.totalProgress.toInt()
            view.findViewById<ProgressBar>(R.id.progressBarHealth).progress = curProgress
            view.findViewById<TextView>(R.id.textView7).text = "$curProgress / ${health.totalProgress}"




        }

        val btn = view.findViewById<FloatingActionButton>(R.id.add_health)
        btn.setOnClickListener {

            update(mydb, builder?.get("name").toString(), curProgress, health.totalProgress.toInt())
            val act = activity as MainActivity
            val frag = update_health_info()
            frag.arguments = arguments


            act.change(frag)

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
         * @return A new instance of fragment update_health_info.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            update_health_info().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getCurrentProgress(name:String):Int{
        val mydb = DatabaseHandler(requireContext())
        val vals = mydb.getAllTasksDate_Dashboard()!!.get(0)

        return vals.get(name)!!.toInt()
    }

    fun update(mydb:DatabaseHandler, name:String, curProgress:Int,totalProgress:Int){
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        mydb.updateDateHealth(currentDate,name,(curProgress!!.toInt()+1).toString())
        val curProgress = getCurrentProgress(name)
        requireView().findViewById<ProgressBar>(R.id.progressBarHealth).progress = curProgress
        requireView().findViewById<TextView>(R.id.cur).text = curProgress.toString()
        requireView().findViewById<TextView>(R.id.textView7).text = "${curProgress} / ${totalProgress}"
    }
}