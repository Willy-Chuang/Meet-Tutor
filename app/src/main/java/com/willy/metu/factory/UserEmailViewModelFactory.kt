package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.chatroom.ChatRoomViewModel
import com.willy.metu.data.source.MeTuRepository

@Suppress("UNCHECKED_CAST")
class UserEmailViewModelFactory constructor(
        private val repository: MeTuRepository,
        private val userEmail: String
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(ChatRoomViewModel::class.java) ->
                        ChatRoomViewModel(repository, userEmail)


                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}