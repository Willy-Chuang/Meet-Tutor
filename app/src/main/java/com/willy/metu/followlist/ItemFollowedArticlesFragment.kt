package com.willy.metu.followlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.databinding.ItemFollowedArticlesBinding
import com.willy.metu.ext.getVmFactory

class ItemFollowedArticlesFragment : Fragment() {

    private val viewModel by viewModels<FollowArticleViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = ItemFollowedArticlesBinding.inflate(inflater, container, false)
        val adapter = ArticleListAdapter()

        binding.viewModel = viewModel
        binding.recyclerSavedArticle.layoutManager = LinearLayoutManager(context)
        binding.recyclerSavedArticle.adapter = adapter


        viewModel.savedArticles.observe( viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })



        return binding.root
    }

}