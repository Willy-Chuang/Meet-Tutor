package com.willy.metu.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Events
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.willy.metu.data.Result

class CalendarBottomSheetViewModel(private val repository: MeTuRepository) : ViewModel() {

    //Get all events with user as attendee

    private var _allEvents = MutableLiveData<List<Events>>()

    val allEvents : LiveData<List<Events>>
        get() = _allEvents

    var allLiveEvents = MutableLiveData<List<Events>>()

    //Query Selected Events

    private var _selectedEvents = MutableLiveData<List<Events>>()

    val selectedEvent : LiveData<List<Events>>
        get() = _selectedEvents

    val selectedLiveEvent = MutableLiveData<List<Events>>()

    //Get value of selectedDate as timestamp

    var currentDate = MutableLiveData<Long>()

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

        getLiveAllEventsResult("willy")

        if (MeTuApplication.instance.isLiveDataDesign()) {
            getLiveAllEventsResult("willy")
        } else {
            getAllEventsResult("willy")
        }
    }

    fun getAllEventsResult(user: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllEvents(user)

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

    fun getLiveAllEventsResult(user: String){
        allLiveEvents = repository.getLiveAllEvents(user)
        _status.value = LoadApiStatus.DONE
    }

}