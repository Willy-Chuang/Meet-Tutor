package com.willy.metu.user

import androidx.lifecycle.ViewModel
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository

class UserDetailViewModel (private val repository: MeTuRepository, private val user: User): ViewModel(){
    val selectedUser = user
}