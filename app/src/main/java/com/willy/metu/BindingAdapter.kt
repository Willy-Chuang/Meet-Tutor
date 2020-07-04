package com.willy.metu

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.calendar.CalendarBottomSheetAdapter
import com.willy.metu.data.Event

@BindingAdapter("events")
fun bindRecyclerView(recyclerView: RecyclerView, eventItem: List<Event>?){
    eventItem?.let{
        recyclerView.adapter?.apply {
            when (this) {
                is CalendarBottomSheetAdapter -> submitList(it)
            }
        }
    }
}