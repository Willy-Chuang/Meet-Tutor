package com.willy.metu.article

import androidx.lifecycle.ViewModel
import com.willy.metu.data.Article
import com.willy.metu.data.source.MeTuRepository

class ArticleViewModel (private val repository: MeTuRepository, private val article: Article) : ViewModel() {

    val fullArticle = article

}