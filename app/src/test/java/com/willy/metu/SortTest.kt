package com.willy.metu

import com.google.common.truth.Truth.assertThat
import com.willy.metu.data.Article
import com.willy.metu.data.Event
import com.willy.metu.ext.sortArticleToMyArticle
import com.willy.metu.ext.sortByTimeStamp
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)

class SortTest {

    // Article Sorting

    private val user1 = Article(creatorEmail = "123")
    private val user2 = Article(creatorEmail = "234")
    private val user3 = Article(creatorEmail = "345")
    private val user4 = Article(creatorEmail = "9527")
    private val listOfArticleWithMe = listOf(user1,user2,user3,user4)
    private val myEmail = "9527"

    @Test
    // Expect to pass
    fun sortMyArticleFromList_Pass() {
        val originSize = listOfArticleWithMe.size
        val filteredSize = listOfArticleWithMe.sortArticleToMyArticle(myEmail).size
        assertThat(originSize - filteredSize).isEqualTo(3)
        assertThat(originSize).isGreaterThan(filteredSize)
    }

    @Test
    // Expect to fail
    fun sortMyArticleFromList_Fail(){
        val originSize = listOfArticleWithMe.size
        val filteredSize = listOfArticleWithMe.sortArticleToMyArticle(myEmail).size
        assertThat(originSize - filteredSize).isNotEqualTo(originSize)
    }

    // Sort Time

    private val event1 = Event(eventTime = 1596968992)
    private val event2 = Event(eventTime = 1596968993)
    private val event3 = Event(eventTime = 1496968993)
    private val event4 = Event(eventTime = 1696968993)
    private val listOfEvent = listOf(event1,event2,event3,event4)

    @Test
    fun sortEventWithinADay_Pass() {
        val selectedTime = 1596931200L
        val resultSize = listOfEvent.sortByTimeStamp(selectedTime).size

        assertThat(resultSize).isEqualTo(2)
        assertThat(resultSize).isGreaterThan(0)
    }



}