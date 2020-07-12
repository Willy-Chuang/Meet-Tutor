package com.willy.metu.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.willy.metu.MainViewModel
import com.willy.metu.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // When Edit button is pressed, navigate to edit mode
        mainViewModel.isPress.observe(viewLifecycleOwner, Observer {
            if (it == true) {
            }

        })


        return binding.root
    }
}