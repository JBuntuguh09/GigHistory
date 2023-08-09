package com.lonewolf.pasco.adaptors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lonewolf.pasco.fragments.Test

class TestAdaptor(fm : FragmentActivity): FragmentStateAdapter(fm) {

    fun getItemName(position: Int):String{
        return when (position){
            0->"Objectives"
            1->"Quiz"
            else->""
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> Test("Objectives")
            1-> Test("Quiz")

            else -> Test("Ojectives")
        }
    }
}