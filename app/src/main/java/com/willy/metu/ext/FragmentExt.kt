package com.willy.metu.ext

import androidx.fragment.app.Fragment
import com.willy.metu.MeTuApplication
import com.willy.metu.factory.PostEventDialogViewModelFactory
import com.willy.metu.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(selectedDate: Long): PostEventDialogViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return PostEventDialogViewModelFactory(repository, selectedDate)
}