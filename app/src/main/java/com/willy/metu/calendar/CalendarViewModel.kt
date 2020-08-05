package com.willy.metu.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Event
import com.willy.metu.data.Result
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.ext.sortByTimeStamp
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import com.willy.metu.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*

class CalendarViewModel(private val repository: MeTuRepository) : ViewModel() {

    //Get all events with user as attendee

    private var _allEvents = MutableLiveData<List<Event>>()

    val allEvent : LiveData<List<Event>>
        get() = _allEvents

    var allLiveEvents = MutableLiveData<List<Event>>()

    //Selected date for safe arg
    private val _navigationToPostDialog = MutableLiveData<Long>()

    val navigationToPostDialog : LiveData<Long>
        get() = _navigationToPostDialog


    //Query Selected Events

    private var _selectedEvents = MutableLiveData<List<Event>>()

    val selectedEvent : LiveData<List<Event>>
        get() = _selectedEvents

    val selectedLiveEvent = MutableLiveData<List<Event>>()


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

        getLiveAllEventsResult(UserManager.user.email)
        todayDate()

        if (MeTuApplication.instance.isLiveDataDesign()) {
            getLiveAllEventsResult(UserManager.user.email)
        } else {
            getAllEventsResult(UserManager.user.email)
        }
    }

    private fun getAllEventsResult(userEmail: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllEvents(userEmail)

            _allEvents.value = when (result) {
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

    private fun getLiveAllEventsResult(userEmail: String){
        allLiveEvents = repository.getLiveAllEvents(userEmail)
    }

    fun getUser(userEmail: String) {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.getUser(userEmail)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
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
    fun createDailyEvent (toTimeStamp: Long) {
        selectedLiveEvent.value = allLiveEvents.value.sortByTimeStamp(toTimeStamp)
        _navigationToPostDialog.value = toTimeStamp
    }

    private fun todayDate() {
        _navigationToPostDialog.value = TimeUtil.dateToStamp(LocalDate.now().toString(), Locale.TAIWAN)
    }

}