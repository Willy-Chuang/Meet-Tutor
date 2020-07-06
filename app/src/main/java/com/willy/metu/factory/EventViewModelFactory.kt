package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.calendar.CalendarBottomSheetViewModel
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.source.MeTuRepository

@Suppress("UNCHECKED_CAST")
class EventViewModelFactory constructor(
    private val repository: MeTuRepository,
    private val selectedEvent: SelectedEvent
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CalendarBottomSheetViewModel::class.java) ->
                    CalendarBottomSheetViewModel(repository)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}