package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.calendar.CalendarBottomSheetViewModel
import com.willy.metu.calendar.PostEventDialogViewModel
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.source.MeTuRepository

@Suppress("UNCHECKED_CAST")
class PostEventDialogViewModelFactory constructor(
    private val repository: MeTuRepository,
    private val selectedDate: Long
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(PostEventDialogViewModel::class.java) ->
                    PostEventDialogViewModel(repository, selectedDate)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}