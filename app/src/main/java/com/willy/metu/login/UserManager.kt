package com.willy.metu.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.willy.metu.MeTuApplication
import com.willy.metu.data.User

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    var userToken: String? = null
        get() = MeTuApplication.instance
                .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
                .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    MeTuApplication.instance
                            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                            .remove(USER_TOKEN)
                            .apply()
                    null
                }
                else -> {
                    MeTuApplication.instance
                            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                            .putString(USER_TOKEN, value)
                            .apply()
                    value
                }
            }
        }

}