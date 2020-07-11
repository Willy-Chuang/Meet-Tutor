package com.willy.metu.pair

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.Answers
import com.willy.metu.data.source.MeTuRepository

class QuestionnaireThreeViewModel (private val repository: MeTuRepository, private val answers: Answers): ViewModel() {

    val isPressed = MutableLiveData<String>()
    val previousAnswers = answers
    var navigateToQuestionFinal = MutableLiveData<Answers>()

}