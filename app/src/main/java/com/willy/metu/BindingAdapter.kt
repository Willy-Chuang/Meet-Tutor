package com.willy.metu

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.willy.metu.calendar.CalendarBottomSheetAdapter
import com.willy.metu.data.Event
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import com.willy.metu.util.TimeUtil

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

@BindingAdapter("ago")
fun bindAgo(textView: TextView, time:Long?){
    time?.let { textView.text = TimeUtil.stampToAgo(time) }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.ic_placeholder)
                                .error(R.drawable.ic_placeholder))
                .into(imgView)
    }
}
@BindingAdapter("setupApiStatus")
fun bindApiStatus(view: ProgressBar, status: LoadApiStatus?) {
    Logger.d("bindApiStatus, status=$status")
    when (status) {
        LoadApiStatus.LOADING -> {
            Logger.d("bindApiStatus=LoadApiStatus.LOADING")
            view.visibility = View.VISIBLE
        }
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
            Logger.d("bindApiStatus=LoadApiStatus.DONE")
            view.visibility = View.GONE
        }
    }
}