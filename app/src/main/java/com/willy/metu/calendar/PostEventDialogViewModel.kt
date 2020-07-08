package com.willy.metu.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Event
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import com.willy.metu.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.willy.metu.data.Result

class PostEventDialogViewModel(
    private val repository: MeTuRepository,
    private val selectedDate: Long
) : ViewModel() {

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    val date = TimeUtil.stampToDate(selectedDate)

    val selectedAttendee = MutableLiveData<String>()

    private val _event = MutableLiveData<SelectedEvent>()

    val event: LiveData<SelectedEvent>
        get() = _event

    // Values ready to be publish

    val title = MutableLiveData<String>()

    val eventTime = MutableLiveData<Long>()

    val invitation = MutableLiveData<String>()

    val type = MutableLiveData<String>()

    val isAllDay = MutableLiveData<Boolean>()

    val startTime = MutableLiveData<Long>()

    val endTime = MutableLiveData<Long>()

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
    }

    fun getEvent(user: String): Event {

        val attendees = listOf<String>(user)

        val invitation = listOf<String>(invitation.value.toString())


        return Event(
            id = "",
            location = location.value.toString(),
            title = title.value.toString(),
            description = description.value.toString(),
            attendees = attendees,
            eventTime = eventTime.value ?: -1,
            invitation = invitation,
            startTime = if (isAllDay.value == true) startTime.value ?: -1 else -1,
            endTime = if (isAllDay.value == true) startTime.value ?: -1 else -1,
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