package com.willy.metu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.willy.metu.component.ProfileAvatarOutlineProvider
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.util.CurrentFragmentType
import com.willy.metu.util.DrawerToggleType
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainViewModel (private val meTuRepository: MeTuRepository): ViewModel(){

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    // According to current fragment to change different drawer toggle
    val currentDrawerToggleType: LiveData<DrawerToggleType> = Transformations.map(currentFragmentType) {
        when (it) {
            CurrentFragmentType.PAIR -> DrawerToggleType.BACK
            else -> DrawerToggleType.NORMAL
        }
    }

    // set up the circle image of an avatar
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val outlineProvider = ProfileAvatarOutlineProvider()

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

}