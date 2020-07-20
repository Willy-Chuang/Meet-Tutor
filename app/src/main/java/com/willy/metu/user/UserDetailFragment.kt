package com.willy.metu.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentUserDetailBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.talentpool.TalentPoolViewModel
import com.willy.metu.util.Logger

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
            binding.imageUrl = it.image

            binding.buttonMessage.setOnClickListener {view ->
                viewModel.createChatRoom(viewModel.getChatRoom())
                findNavController().navigate(NavigationDirections.navigateToChatRoom(it.email, it.name))
            }

        })




        return binding.root
    }

}