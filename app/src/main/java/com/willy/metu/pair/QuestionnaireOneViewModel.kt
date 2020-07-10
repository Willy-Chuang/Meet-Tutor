package com.willy.metu.pair

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.source.MeTuRepository

class QuestionnaireOneViewModel (private val repository: MeTuRepository): ViewModel() {

    val selectedMajorSubject = MutableLiveData<String>()
    val selectedMinorSubject = MutableLiveData<String>()

}