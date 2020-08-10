package com.willy.metu.pair

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.Answers
import com.willy.metu.util.GenderType

class QuestionnaireThreeViewModel(answers: Answers) : ViewModel() {


    private val _isPressed = MutableLiveData<String>()
    val isPressed: LiveData<String>
        get() = _isPressed

    private val previousAnswers = answers
    var navigateToResult = MutableLiveData<Answers>()

    init {
        neutralizeValue()
        addPreviousSelection()
    }

    private fun addPreviousSelection() {
        navigateToResult.value = previousAnswers
    }

    fun setupFemaleButton() {
        if (checkValue() == GenderType.TYPE_FEMALE) {
            _isPressed.value = GenderType.TYPE_NEUTRAL.value
        } else {
            _isPressed.value = GenderType.TYPE_FEMALE.value
        }
    }

    fun setupMaleButton() {
        if (checkValue() == GenderType.TYPE_MALE) {
            _isPressed.value = GenderType.TYPE_NEUTRAL.value
        } else {
            _isPressed.value = GenderType.TYPE_MALE.value
        }
    }

    private fun checkValue(): GenderType {
        return when (isPressed.value) {
            "Male" -> GenderType.TYPE_MALE
            "Female" -> GenderType.TYPE_FEMALE
            else -> GenderType.TYPE_NEUTRAL
        }
    }


    private fun neutralizeValue() {
        _isPressed.value = GenderType.TYPE_NEUTRAL.value
    }


}