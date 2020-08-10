package com.willy.metu.postevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Event
import com.willy.metu.data.Result
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import com.willy.metu.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class PostEventDialogViewModel(
        private val repository: MeTuRepository,
        private val selectedDate: Long
) : ViewModel() {

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    private val _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

    val date = TimeUtil.stampToDate(selectedDate)

    private val _event = MutableLiveData<SelectedEvent>()

    val event: LiveData<SelectedEvent>
        get() = _event

    // Values ready to be publish

    val title = MutableLiveData<String>()

    private val _eventTime = MutableLiveData<Long>()

    val eventTime : LiveData<Long>
        get() = _eventTime

    private val _invitation = MutableLiveData<String>()

    val invitation : LiveData<String>
        get() = _invitation

    private val _type = MutableLiveData<String>()

    val type : LiveData<String>
        get() = _type

    private val _isAllDay =  MutableLiveData<Boolean>()

    val isAllDay : LiveData<Boolean>
        get() = _isAllDay

    private val _startTime = MutableLiveData<Long>()

    val startTime : LiveData<Long>
        get() = _startTime

    private val _endTime = MutableLiveData<Long>()

    val endTime : LiveData<Long>
        get() = _endTime

    val location = MutableLiveData<String>()

    val description = MutableLiveData<String>()


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
        getUser(UserManager.user.email)
        setInitialTime()
    }

    fun getEvent(userEmail: String): Event {

        val attendees = listOf(userEmail)

        val attendeesName = listOf(UserManager.user.name)

        val invitation = listOf(invitation.value.toString())


        return Event(
                id = "",
                location = location.value.toString(),
                title = title.value.toString(),
                description = description.value.toString(),
                attendees = attendees,
                attendeesName = attendeesName,
                eventTime = eventTime.value ?: -1,
                invitation = invitation,
                creatorName = UserManager.user.name,
                creatorImage = UserManager.user.image,
                startTime = if (isAllDay.value == false) startTime.value ?: -1 else -1,
                endTime = if (isAllDay.value == false) endTime.value ?: -1 else -1,
                tag = type.value.toString()
        )
    }

    fun post(event: Event) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postEvent(event)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
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

    fun getUser(userEmail: String) {
        coroutineScope.launch {


            val result = repository.getUser(userEmail)

            _userInfo.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    null
                }
                else -> {
                    _error.value = MeTuApplication.instance.getString(R.string.you_shall_not_pass)
                    null
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

    private fun setInitialTime() {
        _eventTime.value = TimeUtil.dateToStamp(date, Locale.TAIWAN)
    }

    fun setAllDay(answer: Boolean) {
        _isAllDay.value = answer
    }

    fun setEventTime(timeStamp: Long) {
        _eventTime.value = timeStamp
    }

    fun setStartTime(timeStamp: Long) {
        _startTime.value = timeStamp
    }

    fun setEndTime(timeStamp: Long) {
        _endTime.value = timeStamp
    }

    fun setType(selectedType: String){
        _type.value = selectedType
    }

    fun setInvitation(pos: Int) {
        _invitation.value = userInfo.value?.followingEmail?.get(pos - 1).toString()
    }

    fun checkIfComplete(): Boolean {

        return !(title.value == null ||
                eventTime.value == null ||
                invitation.value == null ||
                type.value == null ||
                location.value == null)
    }

}