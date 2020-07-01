package com.willy.metu.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.willy.metu.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentCalendarBinding.inflate(inflater, container, false)

        return binding.root

    }
}