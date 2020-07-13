package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.MainViewModel
import com.willy.metu.calendar.CalendarBottomSheetViewModel
import com.willy.metu.calendar.PostEventDialogViewModel
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.login.LoginViewModel
import com.willy.metu.pair.QuestionnaireOneFragment
import com.willy.metu.pair.QuestionnaireOneViewModel
import com.willy.metu.profile.EditProfileViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val meTuRepository: MeTuRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CalendarBottomSheetViewModel::class.java) ->
                    CalendarBottomSheetViewModel(meTuRepository)
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(meTuRepository)
                isAssignableFrom(QuestionnaireOneViewModel::class.java) ->
                    QuestionnaireOneViewModel(meTuRepository)
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(meTuRepository)
                isAssignableFrom(EditProfileViewModel::class.java) ->
                    EditProfileViewModel(meTuRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}