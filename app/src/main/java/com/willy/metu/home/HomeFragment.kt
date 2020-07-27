package com.willy.metu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentHomeBinding
import com.willy.metu.ext.excludeUser
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortArticleBySubject
import com.willy.metu.ext.sortUserBySubject

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val recommendAdapter = RecommendAdapter()
        val newUserAdapter = NewUserAdapter()
        val articleAdapter = ArticleAdapter(viewModel)

        binding.viewModel = viewModel
        binding.recyclerRecommendation.adapter = recommendAdapter
        binding.recyclerRecommendation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerNewUser.adapter = newUserAdapter
        binding.recyclerNewUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerArticle.adapter = articleAdapter
        binding.recyclerArticle.layoutManager = LinearLayoutManager(context)


        viewModel.allUsers.observe(viewLifecycleOwner, Observer { users ->

            viewModel.biasSubject.observe(viewLifecycleOwner, Observer {

                val sortedList = users.excludeUser().sortUserBySubject(it)

                if (sortedList.isEmpty()) {
                    binding.noValueRecommendation.visibility = View.VISIBLE
                    binding.noValueRecommendationButton.visibility = View.VISIBLE
                    binding.noValueRecommendationButton.setOnClickListener {
                        findNavController().navigate(NavigationDirections.navigateToEditProfileFragment())
                    }
                } else {
                    recommendAdapter.submitList(sortedList)
                }

            })

        })

        viewModel.newUsers.observe(viewLifecycleOwner, Observer {
            newUserAdapter.submitList(it)
        })

        viewModel.oneArticle.observe(viewLifecycleOwner, Observer { article ->

            viewModel.biasSubject.observe(viewLifecycleOwner, Observer {

                val sortedList = article.sortArticleBySubject(it)

                if (sortedList.isEmpty()) {
                    binding.noValueArticles.visibility = View.VISIBLE
                } else {
                    articleAdapter.submitList(article.sortArticleBySubject(it))
                }
            })

        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            binding.userSubject.text = it.tag.component1()
            binding.userSubject2.text = it.tag.component1()
            viewModel.biasSubject.value = it.tag.component1()
        })

        return binding.root
    }
}