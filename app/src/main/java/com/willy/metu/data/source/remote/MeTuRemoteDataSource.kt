package com.willy.metu.data.source.remote

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.*
import com.willy.metu.data.source.MeTuDataSource
import com.willy.metu.login.UserManager
import com.willy.metu.util.Logger
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MeTuRemoteDataSource : MeTuDataSource {

    private const val PATH_EVENTS = "event"
    private const val PATH_USER = "user"
    private const val KEY_CREATED_TIME = "createdTime"
    private const val PATH_CHATLIST = "chatList"
    private const val PATH_ARTICLES = "article"
    private const val PATH_SAVED_ARTICLES = "savedArticles"

    override suspend fun getSelectedEvents(): Result<List<SelectedEvent>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<SelectedEvent>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val event = document.toObject(SelectedEvent::class.java)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>> {
        val liveData = MutableLiveData<List<SelectedEvent>>()
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .addSnapshotListener { snapshot, exception ->
                    Logger.i("add SnapshotListener detected")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<SelectedEvent>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(SelectedEvent::class.java)
                        list.add(event)
                    }
                    liveData.value = list
                }

        return liveData

    }

    override suspend fun getAllEvents(user: String): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .whereArrayContains("attendees", user)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Event>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val event = document.toObject(Event::class.java)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override fun getLiveAllEvents(user: String): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .whereArrayContains("attendees", user)
                .addSnapshotListener { snapshot, exception ->
                    Logger.i("add SnapshotListener detected")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<Event>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    liveData.value = list
                }

        return liveData

    }

    override suspend fun postEvent(event: Event): Result<Boolean> = suspendCoroutine { continuation ->

        val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)
        val document = events.document()
        event.id = document.id

        document
                .set(event)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("Post: $event")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.instance.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override suspend fun postUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->

        val users = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = users.document(user.email)
        user.id = document.id
        user.joinedTime = Calendar.getInstance().timeInMillis

        users.whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        document
                                .set(user)
                                .addOnSuccessListener { documentReference ->
                                    Logger.d("DocumentSnapshot added with ID: ${users}")
                                }
                                .addOnFailureListener { e ->
                                    Logger.w("Error adding document $e")
                                }
                    } else {
                        for (myDocument in result) {
                            Logger.d("Already initialized")
                        }
                    }
                }


    }


    override suspend fun updateUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->
        val users = FirebaseFirestore.getInstance().collection(PATH_USER)
        users.document(user.email)
                .update("introduction", user.introduction ,
                        "experience", user.experience,
                        "city",user.city,
                        "district", user.district,
                        "gender", user.gender,
                        "identity",user.identity,
                        "tag",user.tag)
                .addOnSuccessListener { documentReference ->
                    Logger.d("DocumentSnapshot added with ID: ${users}")
                }
                .addOnFailureListener { e ->
                    Logger.w("Error adding document $e")
                }

    }

    override suspend fun getUser(userEmail: String): Result<User> = suspendCoroutine { continuation ->

        val users = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = users.document(userEmail)

        users.whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var list = User()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)
                            list = user
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
                }


    }

    override suspend fun getAllUsers(): Result<List<User>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<User>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)
                            list.add(user)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean> = suspendCoroutine { continuation ->

        val users = FirebaseFirestore.getInstance().collection(PATH_USER)

        users.document(userEmail).collection("followList").document(user.email)
                .set(user)
                .addOnSuccessListener { documentReference ->
                    Logger.d("DocumentSnapshot added with ID: ${users}")
                }
                .addOnFailureListener { e ->
                    Logger.w("Error adding document $e")
                }

//        users.document(user.email).collection("followedBy").document(userEmail)
//                .set(UserManager.user)
//                .addOnSuccessListener { documentReference ->
//                    Logger.d("DocumentSnapshot added with ID: ${users}")
//                }
//                .addOnFailureListener { e ->
//                    Logger.w("Error adding document $e")
//                }
        users.document(user.email).update("followedBy",FieldValue.arrayUnion(userEmail))
        users.document(userEmail).update("followingEmail",FieldValue.arrayUnion(user.email))
        users.document(userEmail).update("followingName",FieldValue.arrayUnion(user.name))
    }

    override suspend fun getFollowList(userEmail: String): Result<List<User>> = suspendCoroutine { continuation ->
        val users = FirebaseFirestore.getInstance().collection(PATH_USER)

        users.document(userEmail).collection("followList")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<User>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)
                            list.add(user)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> = suspendCoroutine { continuation ->
        val chat = FirebaseFirestore.getInstance().collection(PATH_CHATLIST)
        val document = chat.document()

        chatRoom.chatRoomId = document.id
        chatRoom.latestTime = Calendar.getInstance().timeInMillis

        chat.whereIn("attendees", listOf(chatRoom.attendees))
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        document
                                .set(chatRoom)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Logger.i("Chatroom: $chatRoom")

                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task.exception?.let {

                                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                            return@addOnCompleteListener
                                        }
                                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                                    }
                                }
                    } else {
                        for (myDocument in result) {
                            Logger.d("Already initialized")
                        }
                    }
                }

    }

    override fun getLiveChatRooms(userEmail: String): MutableLiveData<List<ChatRoom>> {
        val liveData = MutableLiveData<List<ChatRoom>>()
        FirebaseFirestore.getInstance()
                .collection(PATH_CHATLIST)
                .orderBy("latestTime", Query.Direction.DESCENDING)
                .whereArrayContains("attendees", userEmail)
                .addSnapshotListener { snapshot, exception ->
                    Logger.i("add SnapshotListener detected")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<ChatRoom>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val chatRoom = document.toObject(ChatRoom::class.java)
                        list.add(chatRoom)
                    }
                    liveData.value = list

                }
        return liveData
    }

    override suspend fun postMessage(emails: List<String>, message: Message): Result<Boolean> = suspendCoroutine { continuation ->

        val chat = FirebaseFirestore.getInstance().collection(PATH_CHATLIST)
        chat.whereIn("attendees", listOf(emails, emails.reversed()))
                .get()
                .addOnSuccessListener { result ->
                    val documentId = chat.document(result.documents[0].id)
                    documentId
                            .update("latestTime", Calendar.getInstance().timeInMillis, "latestMessage", message.text)
                }
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        if (task.exception != null) {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${task.exception!!.message}")
                            continuation.resume(Result.Error(task.exception!!))
                        } else {
                            continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                        }
                    }

                    val documentId2 = chat.document(task.result!!.documents[0].id).collection("message").document()

                    message.createdTime = Calendar.getInstance().timeInMillis
                    message.id = documentId2.id

                    chat.document(task.result!!.documents[0].id).collection("message").add(message)

                }
                .addOnCompleteListener { taskTwo ->
                    if (taskTwo.isSuccessful) {
                        Logger.i("Chatroom: $message")

                        continuation.resume(Result.Success(true))
                    } else {
                        taskTwo.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }

                }

    }

    override fun getAllLiveMessage(emails: List<String>): MutableLiveData<List<Message>> {
        val liveData = MutableLiveData<List<Message>>()

        val chat = FirebaseFirestore.getInstance().collection(PATH_CHATLIST)
        chat.whereIn("attendees", listOf(emails, emails.reversed()))
                .get()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        if (task.exception != null) {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${task.exception!!.message}")
                        } else {
                            Logger.d("sda")
                        }
                    }

                    chat.document(task.result!!.documents[0].id).collection("message")

                            .orderBy("createdTime", Query.Direction.ASCENDING)
                            .addSnapshotListener { snapshot, exception ->
                                Logger.i("add SnapshotListener detected")

                                exception?.let {
                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                }

                                val list = mutableListOf<Message>()
                                for (document in snapshot!!) {
                                    Logger.d(document.id + " => " + document.data)

                                    val message = document.toObject(Message::class.java)
                                    list.add(message)
                                }
                                liveData.value = list

                            }

                }
        return liveData


    }

    override suspend fun postArticle(article: Article): Result<Boolean> = suspendCoroutine { continuation ->
        val articles = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)
        val document = articles.document()

        article.id = document.id
        article.createdTime = Calendar.getInstance().timeInMillis

        document
                .set(article)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("Publish: $article")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override fun getAllLiveArticle(): MutableLiveData<List<Article>> {
        val liveData = MutableLiveData<List<Article>>()

        val articles = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)

        articles
                .addSnapshotListener { snapshot, exception ->
                    Logger.i("add SnapshotListener detected")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<Article>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val article = document.toObject(Article::class.java)
                        list.add(article)
                    }
                    liveData.value = list
                }

        return liveData


    }

    override suspend fun addArticleToWishlist(article: Article, userEmail: String): Result<Boolean> = suspendCoroutine { continuation ->
        val user = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = user.document(userEmail).collection(PATH_SAVED_ARTICLES)

        document
                .whereEqualTo("id", article.id)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        document.document(article.id)
                                .set(article)
                                .addOnSuccessListener { documentReference ->
                                    Logger.d("DocumentSnapshot added with ID: ${article}")
                                }
                                .addOnFailureListener { e ->
                                    Logger.w("Error adding document $e")
                                }
                    } else {
                        document.document(article.id)
                                .delete()

                                .addOnSuccessListener {
                                    Logger.i("Delete: $article")

                                    continuation.resume(Result.Success(true))
                                }.addOnFailureListener {
                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                }
                    }
                }


    }

    override suspend fun getAllSavedArticles(userEmail: String): Result<List<Article>> = suspendCoroutine { continuation ->

        val user = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = user.document(userEmail).collection(PATH_SAVED_ARTICLES)

        document
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Article>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val article = document.toObject(Article::class.java)
                            list.add(article)
                        }


                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }

                }
    }

    override fun getAllLiveSavedArticles(userEmail: String): MutableLiveData<List<Article>> {

        val user = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = user.document(userEmail).collection(PATH_SAVED_ARTICLES)
        val liveData = MutableLiveData<List<Article>>()

        document
                .addSnapshotListener { snapshot, exception ->
                    Logger.i("add SnapshotListener detected")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<Article>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val article = document.toObject(Article::class.java)
                        list.add(article)
                    }
                    liveData.value = list
                }

        return liveData

    }

    override suspend fun removeArticleFromWishlist(article: Article, userEmail: String): Result<Boolean> = suspendCoroutine { continuation ->
        val user = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = user.document(userEmail).collection(PATH_SAVED_ARTICLES)

        document.document(article.id)
                .delete()
                .addOnSuccessListener {
                    Logger.i("Delete: $article")

                    continuation.resume(Result.Success(true))
                }.addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }

    }

    override suspend fun getNewestFiveUsers(): Result<List<User>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .limit(5)
                .orderBy("joinedTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<User>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)
                            list.add(user)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override suspend fun getOneArticle(): Result<List<Article>> = suspendCoroutine { continuation ->

        val articles = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)

        articles
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Article>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val article = document.toObject(Article::class.java)
                            list.add(article)
                        }

                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }

                }
    }


}