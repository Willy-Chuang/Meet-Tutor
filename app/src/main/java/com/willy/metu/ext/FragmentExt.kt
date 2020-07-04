package com.willy.metu.ext

import androidx.fragment.app.Fragment
import com.willy.metu.MeTuApplication
import com.willy.metu.data.Event
import com.willy.metu.factory.EventViewModelFactory
import com.willy.metu.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(event: Event): EventViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return EventViewModelFactory(repository, event)
}