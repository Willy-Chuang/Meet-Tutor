package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.user.UserDetailViewModel

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory constructor(
        private val repository: MeTuRepository,
        private val userEmail: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(UserDetailViewModel::class.java) ->
                        UserDetailViewModel(repository, userEmail)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}