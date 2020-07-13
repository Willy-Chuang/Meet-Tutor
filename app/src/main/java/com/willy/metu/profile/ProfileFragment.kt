package com.willy.metu.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.willy.metu.MainViewModel
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        //Setup chips to show user's tags
        val chipGroup = binding.chipGroup
        val genres = arrayOf("Thriller", "Comedy", "Adventure")
        for (genre in genres) {
            val chip = Chip(chipGroup.getContext())
            chip.text = genre
            chipGroup.addView(chip)
        }

        // When Edit button is pressed, navigate to edit mode
        mainViewModel.isPress.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(NavigationDirections.navigateToEditProfileFragment())
                mainViewModel.isPress.value = false
            }

        })


        return binding.root
    }
}