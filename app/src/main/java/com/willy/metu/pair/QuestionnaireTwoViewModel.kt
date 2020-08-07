package com.willy.metu.pair

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.Answers
import com.willy.metu.data.source.MeTuRepository

class QuestionnaireTwoViewModel(private val repository: MeTuRepository, answers: Answers) : ViewModel() {

    private val _selectedCity = MutableLiveData<String>()
    val selectedCity: LiveData<String>
        get() = _selectedCity

    private val _selectedDistrict = MutableLiveData<String>()
    val selectedDistrict: LiveData<String>
        get() = _selectedDistrict

    private val previousAnswers = answers
    var navigateToQuestionThree = MutableLiveData<Answers>()

    init {
        addPreviousSelection()
    }

    private fun addPreviousSelection() {
        navigateToQuestionThree.value = previousAnswers
    }

    fun setupCity(city: String) {
        _selectedCity.value = city
    }

    fun setupDistrict(district: String) {
        _selectedDistrict.value = district
    }


}