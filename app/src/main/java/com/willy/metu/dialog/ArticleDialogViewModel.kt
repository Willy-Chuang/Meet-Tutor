package com.willy.metu.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Article
import com.willy.metu.data.Result
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArticleDialogViewModel (private val repository: MeTuRepository) : ViewModel(){

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    var articleType = MutableLiveData<String>()
    var articleTitle = MutableLiveData<String>()
    var articleCity = MutableLiveData<String>()
    var articleLocation = MutableLiveData<String>()
    var articleSubject = MutableLiveData<String>()
    var articleDetail = MutableLiveData<String>()


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




    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }

    fun getArticle () : Article {
        return Article(
                id= "",
                createdTime = 0,
                creatorName = UserManager.user.name,
                creatorEmail = UserManager.user.email,
                type = articleType.value.toString(),
                title = articleTitle.value.toString(),
                city = articleCity.value.toString(),
                location = articleLocation.value.toString(),
                subject = articleSubject.value.toString(),
                detail = articleDetail.value.toString()
        )
    }

    fun postArticle (article: Article) {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postArticle(article)) {
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

}