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
import com.willy.metu.databinding.FragmentUserDetailBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager

class UserDetailFragment : Fragment() {

    private val viewModel by viewModels<UserDetailViewModel> {
        getVmFactory(
                UserDetailFragmentArgs.fromBundle(requireArguments()).userEmail
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {

            //Setup chips to show user's tags
            val chipGroup = binding.chipGroup

            val genres = it.tag

            if (genres != null) {
                for (genre in genres) {
                    val chip = Chip(context, null, R.attr.CustomChipChoice)
                    chip.text = genre
                    chipGroup.addView(chip)
                }
            }
            binding.textUserName.text = it.name
            binding.textUserIdentity.text = it.identity
            binding.textProfileCity.text = it.city
            binding.textProfileDistrict.text = it.district
            binding.textIntroduction.text = it.introduction
            binding.textExperience.text = it.experience
            binding.imageUrl = it.image



            if (it.followingEmail.isEmpty()) {
                binding.textFollowing.text = "0"
            } else {
                binding.textFollowing.text = it.followingEmail.size.toString()
            }

            if (it.followedBy.isEmpty()) {
                binding.textFollowBy.text = "0"
            } else {
                binding.textFollowBy.text = it.followedBy.size.toString()
            }

            binding.buttonMessage.setOnClickListener { view ->
                viewModel.createChatRoom(viewModel.getChatRoom())
                Handler().postDelayed({ findNavController().navigate(NavigationDirections.navigateToChatRoom(it.email, it.name)) }, 500)

            }

            binding.buttonFollow.setOnClickListener { view ->
                viewModel.postUserToFollow(UserManager.user.email, it)

            }

            viewModel.getMyArticle(it.email)

            viewModel.myInfo.observe(viewLifecycleOwner, Observer { me ->

                if (me.followingEmail.contains(it.email)) {
                    binding.buttonUnfollow.visibility = View.VISIBLE
                    binding.buttonFollow.visibility = View.GONE
                    binding.buttonUnfollow.setOnClickListener { view ->
                        binding.buttonUnfollow.visibility = View.GONE
                        binding.buttonFollow.visibility = View.VISIBLE
                        viewModel.removeUserFromFollow(UserManager.user.email, it)
                        viewModel.getMyUserInfo(UserManager.user.email)
                    }
                } else {
                    binding.buttonFollow.visibility = View.VISIBLE
                    binding.buttonUnfollow.visibility = View.GONE
                    binding.buttonFollow.setOnClickListener { view ->
                        binding.buttonFollow.visibility = View.GONE
                        binding.buttonUnfollow.visibility = View.VISIBLE
                        viewModel.postUserToFollow(UserManager.user.email, it)
                        viewModel.getMyUserInfo(UserManager.user.email)

                    }

                }

            })


            viewModel.myArticles.observe(viewLifecycleOwner, Observer {
                binding.textPosts.text = it.size.toString()
            })


        })

        return binding.root

    }
}