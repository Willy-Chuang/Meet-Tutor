package com.willy.metu.ext

import androidx.fragment.app.Fragment
import com.willy.metu.MeTuApplication
import com.willy.metu.data.Answers
import com.willy.metu.data.Event
import com.willy.metu.factory.*

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(selectedDate: Long): PostEventDialogViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return PostEventDialogViewModelFactory(repository, selectedDate)
}

fun Fragment.getVmFactory(selectedAnswers: Answers): AnswerViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return AnswerViewModelFactory(repository, selectedAnswers)
}

fun Fragment.getVmFactory(userEmail: String): UserViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return UserViewModelFactory(repository, userEmail)
}

fun Fragment.getVmFactory(userEmail: String, userName: String) : UserEmailViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return UserEmailViewModelFactory(repository, userEmail, userName)
}


fun Fragment.getVmFactory(event: Event) : EventViewModelFactory {
    val repository = (requireContext().applicationContext as MeTuApplication).meTuRepository
    return EventViewModelFactory(repository, event)
}

