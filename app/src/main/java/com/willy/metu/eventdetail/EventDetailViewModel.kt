package com.willy.metu.eventdetail

import androidx.lifecycle.ViewModel
import com.willy.metu.data.Event
import com.willy.metu.data.source.MeTuRepository

class EventDetailViewModel(private val repository: MeTuRepository, event: Event): ViewModel(){

    val event = event

}