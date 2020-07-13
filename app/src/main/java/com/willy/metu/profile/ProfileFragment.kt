package com.willy.metu.profile

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
import com.willy.metu.MainViewModel
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentProfileBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger


class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //Setup chips to show user's tags
//        val chipGroup = binding.chipGroup
//        val genres = viewModel.personalInfo.value?.tag
//
//        if (genres != null) {
//            for (genre in genres) {
//                val chip = Chip(chipGroup.getContext())
//                chip.text = genre
//                chipGroup.addView(chip)
//            }
//        }

        // When Edit button is pressed, navigate to edit mode
        mainViewModel.editIsPressed.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(NavigationDirections.navigateToEditProfileFragment())
                mainViewModel.editIsPressed.value = false
            }

        })

        viewModel.personalInfo.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
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
        })


        return binding.root
    }
}