package com.willy.metu.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Message
import com.willy.metu.data.Result
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatRoomViewModel(private val repository: MeTuRepository, private val userEmail: String, private val userName : String): ViewModel(){

    val currentChattingUser = userEmail

    val currentChattingName = userName

    // EditText input
    val enterMessage = MutableLiveData<String>()

    // All live message
    var allLiveMessage = MutableLiveData<List<Message>>()



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

        getAllLiveMessage(getUserEmails(UserManager.user.email,currentChattingUser))
    }

    fun postMessage(userEmails: List<String>, message: Message) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postMessage(userEmails,message)) {
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

    fun getUserEmails(user1Email: String, user2Email: String) : List<String> {
        return listOf(user1Email, user2Email)
    }

    fun getMessage (): Message {
        return Message (
                id = "",
                senderName = UserManager.user.name,
                senderImage = UserManager.user.image,
                senderEmail = UserManager.user.email,
                text = enterMessage.value.toString(),
                createdTime = 0L
        )
    }

    fun getAllLiveMessage(userEmails: List<String>) {
        allLiveMessage = repository.getAllLiveMessage(userEmails)
        _status.value = LoadApiStatus.DONE
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }



}