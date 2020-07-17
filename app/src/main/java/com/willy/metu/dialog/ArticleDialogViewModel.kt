package com.willy.metu.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.source.MeTuRepository

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




    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }

}