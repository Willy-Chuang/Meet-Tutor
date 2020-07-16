package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.MainViewModel
import com.willy.metu.calendar.CalendarBottomSheetViewModel
import com.willy.metu.calendar.PostEventDialogViewModel
import com.willy.metu.chatlist.ChatListViewModel
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.followlist.FollowListViewModel
import com.willy.metu.login.LoginViewModel
import com.willy.metu.pair.PairingResultViewModel
import com.willy.metu.pair.QuestionnaireOneFragment
import com.willy.metu.pair.QuestionnaireOneViewModel
import com.willy.metu.profile.EditProfileViewModel
import com.willy.metu.profile.ProfileViewModel

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
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(meTuRepository)
                isAssignableFrom(FollowListViewModel::class.java) ->
                    FollowListViewModel(meTuRepository)
                isAssignableFrom(ChatListViewModel::class.java) ->
                    ChatListViewModel(meTuRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}