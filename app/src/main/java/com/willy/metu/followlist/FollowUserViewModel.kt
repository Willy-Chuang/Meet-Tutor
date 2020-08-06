package com.willy.metu.followlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Result
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.ext.sortToOnlyStudents
import com.willy.metu.ext.sortToOnlyTutors
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FollowUserViewModel (private val repository: MeTuRepository) : ViewModel() {

    //For all followed users under user account
    private var _allFollowedList = MutableLiveData<List<User>>()

    val allFollowedList : LiveData<List<User>>
        get() = _allFollowedList

    //For list of following students
    private val _followedStudents = MutableLiveData<List<User>>()

    val followedStudents: LiveData<List<User>>
        get() = _followedStudents


    //For list of following tutors
    private val _followedTutors = MutableLiveData<List<User>>()

    val followedTutors: LiveData<List<User>>
        get() = _followedTutors

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")

        getFollowList(UserManager.user.email)
    }

    private fun getFollowList(userEmail: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getFollowList(userEmail)

            _allFollowedList.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = MeTuApplication.instance.getString(R.string.you_shall_not_pass)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    fun createListOfStudents(userList: List<User>) {
        _followedStudents.value = userList.sortToOnlyStudents()
    }

    fun createListOfTutors(userList: List<User>) {
        _followedTutors.value = userList.sortToOnlyTutors()
    }


}