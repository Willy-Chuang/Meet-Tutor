package com.willy.metu.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Result
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditProfileViewModel (private val repository: MeTuRepository) : ViewModel() {

    //Variables for editable component
    var selectedGender = MutableLiveData<String>()
    var selectedIdentity = MutableLiveData<String>()
    var selectedCity = MutableLiveData<String>()
    var selectedDistrict = MutableLiveData<String>()
    var selectedTags = MutableLiveData<List<String>>()
    val itemList:MutableList<String> = ArrayList()
    var introduction = MutableLiveData<String>()
    var experience = MutableLiveData<String>()



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

    fun updateUser(user: User){
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateUser(user)) {
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

    fun getUserInfo(): User {
        return User (
                id = UserManager.user.email,
                image = UserManager.user.image,
                name = UserManager.user.name,
                email = UserManager.user.email,
                introduction = introduction.value.toString(),
                experience = experience.value.toString(),
                city = selectedCity.value.toString(),
                district = selectedDistrict.value.toString(),
                gender = selectedGender.value.toString(),
                identity = selectedIdentity.value.toString(),
                tag = selectedTags.value!!

        )
    }

    fun checkIfComplete(): Boolean {

        return !(introduction.value == null ||
                selectedDistrict.value == null ||
                selectedCity.value == null ||
                selectedGender.value == null ||
                selectedIdentity.value == null ||
                selectedTags.value == null)
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }


}