package com.willy.metu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.NavigationDirections
import com.willy.metu.R
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

        recommendAdapter.setHasStableIds(true)
        newUserAdapter.setHasStableIds(true)
        articleAdapter.setHasStableIds(true)

        binding.viewModel = viewModel
        binding.recyclerRecommendation.adapter = recommendAdapter
        binding.recyclerRecommendation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerNewUser.adapter = newUserAdapter
        binding.recyclerNewUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerArticle.adapter = articleAdapter
        binding.recyclerArticle.layoutManager = LinearLayoutManager(context)


        viewModel.allUsers.observe(viewLifecycleOwner, Observer { users ->

            binding.recyclerRecommendation.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_fade_in_animation)

            viewModel.biasSubject.observe(viewLifecycleOwner, Observer {

                viewModel.excludeUserFromList(it)

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
            binding.recyclerNewUser.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_fade_in_animation)
            newUserAdapter.submitList(it)
        })

        viewModel.oneArticle.observe(viewLifecycleOwner, Observer { article ->

            viewModel.biasSubject.observe(viewLifecycleOwner, Observer {

                if (it.first().toString() == "") {
                    binding.noValueArticles.visibility = View.VISIBLE

                } else {

                    val sortedList = article.sortArticleBySubject(it)

                    if (sortedList.isEmpty()) {
                        binding.noValueArticles.visibility = View.VISIBLE
                    } else {
                        articleAdapter.submitList(sortedList)
                    }

                }

            })

        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            binding.userSubject.text = it.tag.component1()
            binding.userSubject2.text = it.tag.component1()
            viewModel.biasSubject.value = it.tag.component1()
        })

//        viewModel.status.observe(viewLifecycleOwner, Observer {
//            val progress = binding.progress
//            when (it) {
//                LoadApiStatus.DONE -> {progress.visibility = View.GONE
//                 if (!viewModel.checkIfInfoComplete())  {
//                    Toast.makeText(requireContext(),"Go To Your Mom", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(requireContext(),"Welcome Back", Toast.LENGTH_SHORT).show()
//                } }
//                LoadApiStatus.ERROR -> Toast.makeText(requireContext(),"Internet Failure", Toast.LENGTH_SHORT).show()
//                LoadApiStatus.LOADING -> progress.visibility = View.VISIBLE
//            }
//
//            Logger.i("LOAD${it}")
//        })

        return binding.root
    }
}