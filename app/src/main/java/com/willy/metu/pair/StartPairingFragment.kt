package com.willy.metu.pair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentStartPairingBinding

class StartPairingFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStartPairingBinding.inflate(inflater, container, false)

        binding.buttonStart.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToQuestionOne())
        }

        return binding.root
    }
}