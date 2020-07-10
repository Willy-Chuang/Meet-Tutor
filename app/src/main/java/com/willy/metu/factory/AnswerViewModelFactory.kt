package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.data.Answers
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.pair.QuestionnaireThreeViewModel
import com.willy.metu.pair.QuestionnaireTwoViewModel

@Suppress("UNCHECKED_CAST")
class AnswerViewModelFactory constructor(
        private val repository: MeTuRepository,
        private val answers: Answers
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(QuestionnaireTwoViewModel::class.java) ->
                        QuestionnaireTwoViewModel(repository, answers)
                    isAssignableFrom(QuestionnaireThreeViewModel::class.java) ->
                        QuestionnaireThreeViewModel(repository, answers)


                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}