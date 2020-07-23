package com.willy.metu.eventdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.willy.metu.databinding.FragmentEventDetailBinding
import com.willy.metu.ext.getVmFactory

class EventDetailFragment : Fragment(){

    private val viewModel by viewModels<EventDetailViewModel> {
        getVmFactory(
                EventDetailFragmentArgs.fromBundle(requireArguments()).event
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root

    }
}