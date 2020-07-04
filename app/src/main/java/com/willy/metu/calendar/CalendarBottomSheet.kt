package com.willy.metu.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.willy.metu.R
import com.willy.metu.databinding.BottomSheetCalendarBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger

class CalendarBottomSheet : BottomSheetDialogFragment(){

    private val viewModel by viewModels<CalendarBottomSheetViewModel> {getVmFactory()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ViewDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = BottomSheetCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dialog = this
        isCancelable = false
        binding.recyclerSchedule.layoutManager = LinearLayoutManager(context)
        binding.recyclerSchedule.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        val adapter = CalendarBottomSheetAdapter()
        binding.recyclerSchedule.adapter = adapter

        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            Logger.d("viewModel.liveArticles.observe, it=$it")
            it?.let {
                binding.viewModel = viewModel
            }
        })



        return binding.root
    }

}