package com.example.habitguru.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.habitguru.databaseHelper.DatabaseHandler
import com.example.habitguru.fragments.habit_page
import com.example.habitguru.fragments.habits
import com.example.habitguru.fragments.health

class MyAdapter( val myContext: Context, fm: FragmentManager, var totalTabs: Int) :
    FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        val mydb = DatabaseHandler(myContext)
        val date_list = mydb.getAllTasksDate()
        val date = date_list!!.get(position).get("DATE")

        val bundle = Bundle()
        bundle.putString("date", date)
        Log.d("TAG", "getItem: ")

        val habit_fragment = habit_page()
        habit_fragment.arguments = bundle


        return habit_fragment
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}