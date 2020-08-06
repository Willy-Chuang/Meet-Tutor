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
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.MainViewModel
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentHomeBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        //Setup RecyclerViews

        val recommendAdapter = RecommendAdapter()
        val newUserAdapter = NewUserAdapter()
        val articleAdapter = ArticleAdapter(viewModel)

        binding.recyclerRecommendation.adapter = recommendAdapter
        binding.recyclerNewUser.adapter = newUserAdapter
        binding.recyclerArticle.adapter = articleAdapter


        viewModel.newUsers.observe(viewLifecycleOwner, Observer {

            setupRecyclerAnimation(binding.recyclerNewUser)
            newUserAdapter.submitList(it)

        })


        viewModel.biasSubject.observe(viewLifecycleOwner, Observer {

            // Filter new users if they have default value
            if (it == "") {
                recommendationValueVisibility(false)
                relatedArticleValueVisibility(false)
            } else {
                // Filter all users based on the user's bias subject, then submit list as recommendation
                viewModel.recommendedUserList.observe(viewLifecycleOwner, Observer {users ->

                    setupRecyclerAnimation(binding.recyclerRecommendation)

                    if (users.isNullOrEmpty()) {
                        recommendationValueVisibility(false)
                    } else {
                        recommendationValueVisibility(true)
                        recommendAdapter.submitList(users)
                    }
                })
                // Filter all articles based on the user's bias subject, then submit list as related
                viewModel.recommendedArticleList.observe(viewLifecycleOwner, Observer {articles ->

                    if (articles.isNullOrEmpty()) {
                        relatedArticleValueVisibility(false)
                    } else {
                        relatedArticleValueVisibility(true)
                        articleAdapter.submitList(articles)
                    }
                })
            }
        })


        viewModel.userInfo.observe(viewLifecycleOwner, Observer {

            // Show new member dialog if his/hers info isn't complete
            if (!viewModel.checkIfInfoComplete()) {
                if (mainViewModel.noticed.value == false) {
                    findNavController().navigate(NavigationDirections.navigateToNewMember())
                    mainViewModel.noticed.value = true
                } else {
                    Toast.makeText(requireContext(), getString(R.string.reminder_user_info), Toast.LENGTH_LONG).show()
                }
            } else {
                Logger.i(getString(R.string.logger_user_return))
            }

            // Get the first subject of user as bias subject
            if (it.tag.isNullOrEmpty()) {
                Logger.i(getString(R.string.logger_no_bias))
            } else {
                setBiasSubject(it.tag.component1())
            }

        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                }
            }
        })

        return binding.root
    }

    private fun setupRecyclerAnimation(recyclerView: RecyclerView) {
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_fade_in_animation)
    }

    private fun setBiasSubject(subject: String) {
        binding.userSubject.text = subject
        binding.userSubject2.text = subject
        viewModel.setBiasSubject(subject)
    }

    private fun recommendationValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValueRecommendation.visibility = View.GONE
            binding.noValueRecommendationButton.visibility = View.GONE
        } else {
            binding.noValueRecommendation.visibility = View.VISIBLE
            binding.noValueRecommendationButton.visibility = View.VISIBLE
            binding.noValueRecommendationButton.setOnClickListener {
                findNavController().navigate(NavigationDirections.navigateToEditProfileFragment())
            }
        }
    }

    private fun relatedArticleValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValueArticles.visibility = View.GONE
        } else {
            binding.noValueArticles.visibility = View.VISIBLE
        }
    }
}