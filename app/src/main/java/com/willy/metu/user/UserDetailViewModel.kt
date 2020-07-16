package com.willy.metu.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.ChatRoom
import com.willy.metu.data.Result
import com.willy.metu.data.User
import com.willy.metu.data.UserInfo
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserDetailViewModel (private val repository: MeTuRepository, private val user: User): ViewModel(){

    val selectedUser = user

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

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

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
    }

//    fun getChatRoom():ChatRoom {
//
//        var attendeeNames = listOf(UserManager.user.name, selectedUser.name)
//        var attendeeImages = listOf(UserManager.user.image, selectedUser.image)
//        return ChatRoom(
//                chatRoomId = "",
//                latestTime = 0,
//                attendeeImages = attendeeImages,
//                attendeeNames = attendeeNames,
//                userName = UserManager.user.name
//        )
//    }

    fun getChatRoom(): ChatRoom {

        var attendeeOne = UserInfo().apply {
            userEmail = UserManager.user.email
            userImage = UserManager.user.image
            userName = UserManager.user.name
        }

        var attendeeTwo = UserInfo().apply {
            userEmail = selectedUser.email
            userImage = selectedUser.image
            userName = selectedUser.name
        }

        var attendeeList = listOf(UserManager.user.email, selectedUser.email)

        return ChatRoom(
                chatRoomId = "",
                latestTime = 0,
                attendeesInfo = listOf(attendeeOne,attendeeTwo),
                attendees = attendeeList
        )
    }


    fun createChatRoom(chatRoom: ChatRoom) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postChatRoom(chatRoom)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MeTuApplication.instance.getString(R.string.you_shall_not_pass)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }

    }


    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }


}