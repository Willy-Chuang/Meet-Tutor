package com.willy.metu.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.willy.metu.databinding.FragmentUserDetailBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger

class UserDetailFragment : Fragment(){

    private val viewModel by viewModels<UserDetailViewModel> {
        getVmFactory(
            UserDetailFragmentArgs.fromBundle(requireArguments()).user
        )
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        //Setup chips to show user's tags
        val chipGroup = binding.chipGroup

        val genres = viewModel.selectedUser.tag

        if (genres != null) {
            for (genre in genres) {
                val chip = Chip(chipGroup.getContext())
                chip.text = genre
                chipGroup.addView(chip)
            }
        }


        return binding.root
    }

}