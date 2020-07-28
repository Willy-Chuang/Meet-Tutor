package com.willy.metu.eventdetail

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentEventDetailBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager

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

        if(viewModel.event.startTime.equals(-1)){
            binding.layoutStartEndTime.visibility = View.GONE
        } else {
            binding.layoutStartEndTime.visibility = View.VISIBLE
        }

        binding.textAttenddes.text = viewModel.event.attendeesName.first()

        binding.buttonDecline.setOnClickListener {
            viewModel.declineEvent(viewModel.event, UserManager.user.email)
            findNavController().navigate(NavigationDirections.navigateToNotify())
        }

        binding.buttonAccept.setOnClickListener {
            viewModel.acceptEvent(viewModel.event, UserManager.user.email, UserManager.user.name)
            findNavController().navigate(NavigationDirections.navigateToNotify())
        }

        return binding.root

    }
}