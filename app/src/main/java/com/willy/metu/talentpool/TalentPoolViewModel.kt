package com.willy.metu.talentpool

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

class TalentPoolViewModel(private val repository: MeTuRepository) : ViewModel() {

    var allLiveArticles = MutableLiveData<List<Article>>()

    var savedArticles = MutableLiveData<List<Article>>()

    private var _checked = MutableLiveData<Boolean>()

    val checked: LiveData<Boolean>
        get() = _checked

    private var _selectedType = MutableLiveData<String>()

    val selectedType: LiveData<String>
        get() = _selectedType

    // Deleted article title for snack bar to show
    private val _deletedArticleTitle = MutableLiveData<String>()

    val deletedArticleTitle: LiveData<String>
        get() = _deletedArticleTitle

    // status: The internal MutableLiveData that stores the status of the most recent request
    private var _status = MutableLiveData<LoadApiStatus>()

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

        getAllLiveSavedArticles(UserManager.user.email)
        getAllLiveArticles()

    }

    private fun getAllLiveArticles() {
        allLiveArticles = repository.getAllLiveArticle()
        _status.value = LoadApiStatus.DONE
    }

    fun addArticlesToWishlist(article: Article, userEmail: String) {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.addArticleToWishlist(article, userEmail)) {
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

    private fun getAllLiveSavedArticles(userEmail: String) {
        savedArticles = repository.getAllLiveSavedArticles(userEmail)
        _status.value = LoadApiStatus.DONE
    }

    fun delete(article: Article) {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.deleteArticle(article)) {
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

    fun setType(type: String) {
        _selectedType.value = type
    }

    fun isChecked(value: Boolean) {
        _checked.value = value
    }

    fun passDeletedTitle(title: String) {
        _deletedArticleTitle.value = title
    }


}