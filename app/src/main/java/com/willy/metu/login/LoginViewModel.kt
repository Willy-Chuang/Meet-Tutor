package com.willy.metu.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository

class LoginViewModel(private val meTuRepository: MeTuRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user
}