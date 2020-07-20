package com.willy.metu.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.article.ArticleViewModel
import com.willy.metu.data.Answers
import com.willy.metu.data.Article
import com.willy.metu.data.source.MeTuRepository

@Suppress("UNCHECKED_CAST")
class ArticleViewModelFactory constructor(
        private val repository: MeTuRepository,
        private val article: Article
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(ArticleViewModel::class.java) ->
                        ArticleViewModel(repository, article)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}