package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.data.Event
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.eventdetail.EventDetailViewModel

@Suppress("UNCHECKED_CAST")
class EventViewModelFactory constructor(
        private val repository: MeTuRepository,
        private val event: Event
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(EventDetailViewModel::class.java) ->
                        EventDetailViewModel(repository, event)


                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}