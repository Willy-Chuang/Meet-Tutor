package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.willy.metu.data.*

interface MeTuRepository {

    suspend fun getSelectedEvents(): Result<List<SelectedEvent>>

    fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>>

    suspend fun getAllEvents(event: String): Result<List<Event>>

    fun getLiveAllEvents(event: String): MutableLiveData<List<Event>>

    suspend fun postEvent(event: Event): Result<Boolean>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun updateUser (user: User): Result<Boolean>

    suspend fun getUser (userEmail: String): Result<User>

    suspend fun getAllUsers():Result<List<User>>

    suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean>

    suspend fun getFollowList(userEmail: String): Result<List<User>>

    suspend fun postChatRoom(chatRoom: ChatRoom):Result<Boolean>

    fun getLiveChatRooms(userEmail: String): MutableLiveData<List<ChatRoom>>

    suspend fun postMessage(emails: List<String>, message: Message): Result<Boolean>

    fun getAllLiveMessage (emails: List<String>) : MutableLiveData<List<Message>>

    suspend fun postArticle(article: Article): Result<Boolean>

    suspend fun deleteArticle(article: Article): Result<Boolean>

    fun getAllLiveArticle(): MutableLiveData<List<Article>>

    suspend fun addArticleToWishlist(article: Article, userEmail: String): Result<Boolean>

    suspend fun getAllSavedArticles(userEmail: String): Result<List<Article>>

    fun getAllLiveSavedArticles(userEmail: String): MutableLiveData<List<Article>>

    suspend fun removeArticleFromWishlist(article: Article, userEmail: String): Result<Boolean>

    suspend fun getNewestFiveUsers(): Result<List<User>>

    suspend fun getOneArticle(): Result<List<Article>>

    suspend fun getMyArticle(userEmail: String): Result<List<Article>>

    suspend fun removeUserFromFollow(userEmail: String, user: User): Result<Boolean>

    fun getLiveMyEventInvitation(userEmail: String): MutableLiveData<List<Event>>

    suspend fun acceptEvent(event: Event, userEmail: String, userName: String): Result<Boolean>

    suspend fun declineEvent(event: Event, userEmail: String): Result<Boolean>

    suspend fun firebaseAuthWithGoogle(account : GoogleSignInAccount?): Result<FirebaseUser>


}