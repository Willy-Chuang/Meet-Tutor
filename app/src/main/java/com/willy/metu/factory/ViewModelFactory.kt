package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.data.source.MeTuRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: MeTuRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
            }
        } as T
}