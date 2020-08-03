package com.willy.metu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.MainViewModel
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentHomeBinding
import com.willy.metu.ext.excludeUser
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortArticleBySubject
import com.willy.metu.ext.sortUserBySubject
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        binding.viewModel = viewModel

        //Setup RecyclerViews

        val recommendAdapter = RecommendAdapter()
        val newUserAdapter = NewUserAdapter()
        val articleAdapter = ArticleAdapter(viewModel)

        recommendAdapter.setHasStableIds(true)
        newUserAdapter.setHasStableIds(true)
        articleAdapter.setHasStableIds(true)

        binding.recyclerRecommendation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerRecommendation.adapter = recommendAdapter

        binding.recyclerNewUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerNewUser.adapter = newUserAdapter

        binding.recyclerArticle.layoutManager = LinearLayoutManager(context)
        binding.recyclerArticle.adapter = articleAdapter


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

                Logger.i("bias subject = $it")

                if (it == "") {
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

            if (!viewModel.checkIfInfoComplete()) {
                if (mainViewModel.noticed.value == false) {
                    findNavController().navigate(NavigationDirections.navigateToFinishInfo())
                    mainViewModel.noticed.value = true
                } else {
                    Toast.makeText(requireContext(), "Remember to complete your profile", Toast.LENGTH_LONG).show()
                }

            } else {
                Logger.i("User Is Back")
            }

            if (it.tag.isNullOrEmpty()) {
                Logger.i("Brand New User")

            } else {
                binding.userSubject.text = it.tag.component1()
                binding.userSubject2.text = it.tag.component1()
                viewModel.biasSubject.value = it.tag.component1()
            }

        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            Logger.d("viewModel.test.observe=LoadApiStatus.LOADING")
            when (it) {
                LoadApiStatus.LOADING -> {
                    Logger.d("viewModel.test.observe=LoadApiStatus.LOADING")
                    binding.progress.visibility = View.VISIBLE

                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
                    Logger.d("viewModel.test.observe=LoadApiStatus.DONE")
                    binding.progress.visibility = View.GONE

                }
            }
        })

        return binding.root
    }
}