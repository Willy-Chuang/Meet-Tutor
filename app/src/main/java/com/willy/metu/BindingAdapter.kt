package com.willy.metu

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.calendar.CalendarBottomSheetAdapter
import com.willy.metu.data.SelectedEvent
import com.willy.metu.util.TimeUtil

@BindingAdapter("events")
fun bindRecyclerView(recyclerView: RecyclerView, selectedEventItem: List<SelectedEvent>?){
    selectedEventItem?.let{
        recyclerView.adapter?.apply {
            when (this) {
                is CalendarBottomSheetAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("month")
fun bindMonth(textView: TextView, time: Long?) {
    time?.let {textView.text = TimeUtil.stampToMonth(time)}
}

@BindingAdapter("day")
fun bindDate(textView: TextView, time: Long?){
    time?.let {textView.text = TimeUtil.stampToDayOfMonth(time)}
}

@BindingAdapter("weekday")
fun bindWeekday(textView: TextView, time: Long?){
    time?.let {textView.text = TimeUtil.stampToWeekday(time)}
}

@BindingAdapter("time")
fun bindTime(textView: TextView, time: Long?){
    time?.let {textView.text = TimeUtil.stampToTime(time)}
}