package com.willy.metu.pair

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.Answers
import com.willy.metu.data.source.MeTuRepository

class QuestionnaireTwoViewModel (private val repository: MeTuRepository, private val answers: Answers): ViewModel() {

    val selectedCity = MutableLiveData<String>()
    val selectedDistrict = MutableLiveData<String>()
    val previousAnswers = answers
    var navigateToQuestionThree = MutableLiveData<Answers>()

}