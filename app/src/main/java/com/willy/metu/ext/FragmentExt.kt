package com.willy.metu.ext

import androidx.fragment.app.Fragment
import com.willy.metu.MeTuApplication
import com.willy.metu.data.SelectedEvent
import com.willy.metu.factory.EventViewModelFactory
import com.willy.metu.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(selectedEvent: SelectedEvent): EventViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return EventViewModelFactory(repository, selectedEvent)
}