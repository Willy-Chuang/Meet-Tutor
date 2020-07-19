package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.*

class DefaultMeTuRepository(
    private val remoteDataSource: MeTuDataSource,
    private val localDataSource: MeTuDataSource
) : MeTuRepository {

    override suspend fun getSelectedEvents(): Result<List<SelectedEvent>> {
        return remoteDataSource.getSelectedEvents()
    }

    override fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>> {
        return remoteDataSource.getLiveSelectedEvents()
    }

    override suspend fun getAllEvents(user: String): Result<List<Event>> {
        return remoteDataSource.getAllEvents(user)
    }

    override fun getLiveAllEvents(user: String): MutableLiveData<List<Event>> {
        return remoteDataSource.getLiveAllEvents(user)
    }

    override suspend fun postEvent(event: Event): Result<Boolean> {
        return remoteDataSource.postEvent(event)
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        return remoteDataSource.postUser(user)
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        return remoteDataSource.updateUser(user)
    }

    override suspend fun getUser(userEmail: String): Result<User> {
        return remoteDataSource.getUser(userEmail)
    }

    override suspend fun getAllUsers():Result<List<User>> {
        return remoteDataSource.getAllUsers()
    }

    override suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean>{
        return remoteDataSource.postUserToFollow(userEmail, user)
    }

    override suspend fun getFollowList(userEmail: String): Result<List<User>> {
        return remoteDataSource.getFollowList(userEmail)
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> {
        return remoteDataSource.postChatRoom(chatRoom)
    }

    override fun getLiveChatRooms(userEmail: String): MutableLiveData<List<ChatRoom>> {
        return remoteDataSource.getLiveChatRooms(userEmail)
    }

    override suspend fun postMessage(emails: List<String>, message: Message): Result<Boolean> {
        return remoteDataSource.postMessage(emails, message)
    }

    override fun getAllLiveMessage (emails: List<String>) : MutableLiveData<List<Message>> {
        return remoteDataSource.getAllLiveMessage(emails)
    }

    override suspend fun postArticle(article: Article): Result<Boolean> {
        return remoteDataSource.postArticle(article)
    }

    override fun getAllLiveArticle(): MutableLiveData<List<Article>> {
        return remoteDataSource.getAllLiveArticle()
    }

    override suspend fun addArticleToWishlist(article: Article, userEmail: String): Result<Boolean> {
        return remoteDataSource.addArticleToWishlist(article, userEmail)
    }

    override suspend fun getAllSavedArticles(userEmail: String): Result<List<Article>> {
        return remoteDataSource.getAllSavedArticles(userEmail)
    }

    override fun getAllLiveSavedArticles(userEmail: String): MutableLiveData<List<Article>> {
        return remoteDataSource.getAllLiveSavedArticles(userEmail)
    }

    override suspend fun removeArticleFromWishlist(article: Article, userEmail: String): Result<Boolean> {
        return remoteDataSource.removeArticleFromWishlist(article,userEmail)
    }
}