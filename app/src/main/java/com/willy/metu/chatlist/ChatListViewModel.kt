package com.willy.metu.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.ChatRoom
import com.willy.metu.data.Result
import com.willy.metu.data.UserInfo
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatListViewModel (private val repository: MeTuRepository): ViewModel(){

    var allLiveChatRooms = MutableLiveData<List<ChatRoom>>()

    var filteredChatRooms = MutableLiveData<List<ChatRoom>>()

    var theOtherPersonInfo = MutableLiveData<List<UserInfo>>()

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

        getAllLiveChatRoom(UserManager.user.email)
    }

    fun getAllLiveChatRoom(userEmail:String) {
        allLiveChatRooms = repository.getLiveChatRooms(userEmail)
        _status.value = LoadApiStatus.DONE
    }



}