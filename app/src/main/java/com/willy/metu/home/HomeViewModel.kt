package com.willy.metu.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Article
import com.willy.metu.data.Result
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.ext.excludeUser
import com.willy.metu.ext.sortUserBySubject
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MeTuRepository) : ViewModel() {

    private val _allUsers = MutableLiveData<List<User>>()

    val allUsers: LiveData<List<User>>
        get() = _allUsers

    val biasSubject = MutableLiveData<String>()


    private val _newUsers = MutableLiveData<List<User>>()

    val newUsers: LiveData<List<User>>
        get() = _newUsers

    private val _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

    private val _oneArticle = MutableLiveData<List<Article>>()

    val oneArticle: LiveData<List<Article>>
        get() = _oneArticle


    var savedArticles = MutableLiveData<List<Article>>()

    var isAdded = MutableLiveData<Boolean>()


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

        getUser(UserManager.user.email)
        getNewestFiveUsers()
        getAllUsers()
        getAllLiveSavedArticles(UserManager.user.email)
        getOneArticle()
    }

    var doneProgressCount = 4
    fun doneProgress() {

        doneProgressCount--
        if (doneProgressCount == 0) _status.value = LoadApiStatus.DONE
    }

    fun getNewestFiveUsers() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getNewestFiveUsers()

            _newUsers.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    doneProgress()
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

    fun getAllUsers() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllUsers()

            _allUsers.value = when (result) {
                is Result.Success -> {
                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
                    doneProgress()
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

    fun getUser(userEmail: String) {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getUser(userEmail)

            _userInfo.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    doneProgress()
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


    // Articles
    fun getOneArticle() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getOneArticle()

            _oneArticle.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    doneProgress()
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

    fun addArticlesToWishlist(article: Article, userEmail: String) {
        coroutineScope.launch {

            when (val result = repository.addArticleToWishlist(article, userEmail)) {
                is Result.Success -> {
                    _error.value = null
                    isAdded.value = result.data
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

    fun getAllLiveSavedArticles(userEmail: String) {
        savedArticles = repository.getAllLiveSavedArticles(userEmail)
    }

    fun checkIfInfoComplete(): Boolean {
        val userInfo = userInfo.value
        return !(userInfo?.identity == "" && userInfo?.tag.isNullOrEmpty())
    }

    fun excludeUserFromList(subject: String) {
        allUsers.value.excludeUser().sortUserBySubject(subject)
    }


    fun onLoaded() {
        _status.value = LoadApiStatus.DONE
    }
}