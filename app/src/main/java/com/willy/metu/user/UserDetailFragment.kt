package com.willy.metu.user

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.data.User
import com.willy.metu.databinding.FragmentUserDetailBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager

class UserDetailFragment : Fragment() {

    private val viewModel by viewModels<UserDetailViewModel> {
        getVmFactory(
                UserDetailFragmentArgs.fromBundle(requireArguments()).userEmail
        )
    }

    lateinit var binding: FragmentUserDetailBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // Setup back button
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {

            setupLayout(it)

            // Get user article to set count on the list
            viewModel.myArticles.observe(viewLifecycleOwner, Observer { list ->
                binding.textPosts.text = list.size.toString()
            })

            // Check if the user is in my follow list already, and setup follow button base on the result
            viewModel.myInfo.observe(viewLifecycleOwner, Observer { my ->

                if (my.followingEmail.contains(it.email)) {
                    showFollowButton(false)
                    setupFollowButton(true, it)
                } else {
                    showFollowButton(true)
                    setupFollowButton(false, it)
                }

            })

        })

        return binding.root

    }


    private fun setupLayout(user: User) {
        val chipGroup = binding.chipGroup
        val genres = user.tag

        for (genre in genres) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chipGroup.addView(chip)
        }

        binding.buttonMessage.setOnClickListener {
            viewModel.createChatRoom(viewModel.getChatRoom())
            Handler().postDelayed({ findNavController().navigate(NavigationDirections.navigateToChatRoom(user.email, user.name)) }, 500)
        }

    }

    private fun showFollowButton(show: Boolean) {
        if (show) {
            binding.buttonFollow.visibility = View.VISIBLE
            binding.buttonUnfollow.visibility = View.GONE
        } else {
            binding.buttonUnfollow.visibility = View.VISIBLE
            binding.buttonFollow.visibility = View.GONE
        }
    }

    private fun setupFollowButton(contains: Boolean, user: User) {
        if (contains) {
            binding.buttonUnfollow.setOnClickListener {
                showFollowButton(true)
                viewModel.removeUserFromFollow(UserManager.user.email, user)
                viewModel.getMyUserInfo(UserManager.user.email)
            }
        } else {
            binding.buttonFollow.setOnClickListener {
                showFollowButton(false)
                viewModel.postUserToFollow(UserManager.user.email, user)
                viewModel.getMyUserInfo(UserManager.user.email)

            }

        }
    }
}