package com.willy.metu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.willy.metu.component.ProfileAvatarOutlineProvider
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.CurrentFragmentType
import com.willy.metu.util.DrawerToggleType
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainViewModel (private val repository: MeTuRepository): ViewModel(){

    // user: MainViewModel has User info to provide Drawer UI
    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    // Edit button is pressed

    val editIsPressed = MutableLiveData<Boolean>()
    val saveIsPressed = MutableLiveData<Boolean>()

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    // According to current fragment to change different drawer toggle
    val currentDrawerToggleType: LiveData<DrawerToggleType> = Transformations.map(currentFragmentType) {
        when (it) {
            CurrentFragmentType.CALENDAR -> DrawerToggleType.BACK
            CurrentFragmentType.PROFILE -> DrawerToggleType.BACK
            CurrentFragmentType.EDITPROFILE -> DrawerToggleType.BACK
            else -> DrawerToggleType.NORMAL
        }
    }

    // set up the circle image of an avatar
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val outlineProvider = ProfileAvatarOutlineProvider()

    private val _refresh = MutableLiveData<Boolean>()

    val refresh: LiveData<Boolean>
        get() = _refresh

    // Handle navigation to home by bottom nav directly which includes icon change
    private val _navigateToHomeByBottomNav = MutableLiveData<Boolean>()

    val navigateToHomeByBottomNav: LiveData<Boolean>
        get() = _navigateToHomeByBottomNav

    // Handle navigation to pairing by bottom nav directly which includes icon change
    private val _navigateToPairingByBottomNav = MutableLiveData<Boolean>()

    val navigateToPairingByBottomNav: LiveData<Boolean>
        get() = _navigateToPairingByBottomNav

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

    fun refresh() {
        if (!MeTuApplication.instance.isLiveDataDesign()) {
            _refresh.value = true
        }
    }

    fun onRefreshed() {
        if (!MeTuApplication.instance.isLiveDataDesign()) {
            _refresh.value = null
        }
    }

    fun onHomeNavigated() {
        _navigateToHomeByBottomNav.value = null
    }

    fun setupUser(user: User) {

        _user.value = user
        Logger.i("=============")
        Logger.i("| setupUser |")
        Logger.i("user=$user")
        Logger.i("MainViewModel=${this}")
        Logger.i("=============")
    }


//    fun checkUser() {
//        if (user.value == null) {
//            UserManager.userToken?.let {
//                getUserProfile(it)
//            }
//        }
//    }

}