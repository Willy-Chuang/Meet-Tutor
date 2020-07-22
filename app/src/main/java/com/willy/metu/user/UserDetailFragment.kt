package com.willy.metu.user

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentUserDetailBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager
import com.willy.metu.talentpool.TalentPoolViewModel
import com.willy.metu.util.Logger
import kotlinx.android.synthetic.main.activity_main.*

class UserDetailFragment : Fragment(){

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
//        val talentpoolViewModel = ViewModelProvider(this).get(TalentPoolViewModel::class.java)
//
//        talentpoolViewModel.user.value = null

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {

            //Setup chips to show user's tags
            val chipGroup = binding.chipGroup

            val genres = it.tag

            if (genres != null) {
                for (genre in genres) {
                    val chip = Chip(chipGroup.getContext())
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

            if(it.followingEmail.component1() == ""){
                binding.textFollowing.text = "0"
            }else {
                binding.textFollowing.text = it.followingEmail.size.toString()
            }

            if(it.followedBy.component1() == ""){
                binding.textFollowBy.text = "0"
            }else {
                binding.textFollowBy.text = it.followedBy.size.toString()
            }

            binding.buttonMessage.setOnClickListener { view ->
                viewModel.createChatRoom(viewModel.getChatRoom())
                Handler().postDelayed({findNavController().navigate(NavigationDirections.navigateToChatRoom(it.email, it.name))},500)

            }

            binding.buttonFollow.setOnClickListener { view ->
                viewModel.postUserToFollow(UserManager.user.email, it)

            }

        })




        return binding.root
    }

}