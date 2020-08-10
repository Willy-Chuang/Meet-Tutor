package com.willy.metu.pair

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.source.MeTuRepository

class QuestionnaireOneViewModel(private val repository: MeTuRepository) : ViewModel() {

    private val _selectedMajorSubject = MutableLiveData<String>()

    val selectedMajorSubject: LiveData<String>
        get() = _selectedMajorSubject

    private val _selectedMinorSubject = MutableLiveData<String>()

    val selectedMinorSubject: LiveData<String>
        get() = _selectedMinorSubject


    fun setupMinorSubject(subject: String) {
        _selectedMinorSubject.value = subject
    }

    fun setupCategory(category: String) {
        _selectedMajorSubject.value = category
    }

}