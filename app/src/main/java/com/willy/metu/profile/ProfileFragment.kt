package com.willy.metu.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.willy.metu.MainViewModel
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.data.User
import com.willy.metu.databinding.FragmentProfileBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger


class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }
    lateinit var binding: FragmentProfileBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        // When Edit button is pressed, navigate to edit mode
        mainViewModel.editIsPressed.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(NavigationDirections.navigateToEditProfileFragment())
                mainViewModel.editIsPressed.value = false
            }
        })

        viewModel.personalInfo.observe(viewLifecycleOwner, Observer {

            setupChips(it)

            Logger.i(it.toString())
        })


        return binding.root
    }

    private fun setupChips(user: User) {
        val chipGroup = binding.chipGroup
        val subjects = user.tag

        for (subject in subjects) {

            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = subject
            chipGroup.addView(chip)
        }
    }
}