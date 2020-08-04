package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.MainViewModel
import com.willy.metu.calendar.CalendarBottomSheetViewModel
import com.willy.metu.chatlist.ChatListViewModel
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.dialog.ArticleDialogViewModel
import com.willy.metu.followlist.FollowArticleViewModel
import com.willy.metu.followlist.FollowUserViewModel
import com.willy.metu.home.HomeViewModel
import com.willy.metu.login.LoginViewModel
import com.willy.metu.newchat.NewChatViewModel
import com.willy.metu.notify.NotifyViewModel
import com.willy.metu.pair.QuestionnaireOneViewModel
import com.willy.metu.editprofile.EditProfileViewModel
import com.willy.metu.profile.ProfileViewModel
import com.willy.metu.talentpool.TalentPoolViewModel

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
                isAssignableFrom(FollowUserViewModel::class.java) ->
                    FollowUserViewModel(meTuRepository)
                isAssignableFrom(ChatListViewModel::class.java) ->
                    ChatListViewModel(meTuRepository)
                isAssignableFrom(ArticleDialogViewModel::class.java) ->
                    ArticleDialogViewModel(meTuRepository)
                isAssignableFrom(TalentPoolViewModel::class.java) ->
                    TalentPoolViewModel(meTuRepository)
                isAssignableFrom(FollowArticleViewModel::class.java) ->
                    FollowArticleViewModel(meTuRepository)
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(meTuRepository)
                isAssignableFrom(NotifyViewModel::class.java) ->
                    NotifyViewModel(meTuRepository)
                isAssignableFrom(NewChatViewModel::class.java) ->
                    NewChatViewModel(meTuRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}