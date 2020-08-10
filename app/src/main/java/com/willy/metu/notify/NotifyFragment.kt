package com.willy.metu.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.willy.metu.databinding.FragmentNotifyBinding
import com.willy.metu.ext.getVmFactory

class NotifyFragment : Fragment() {

    private val viewModel by viewModels<NotifyViewModel> { getVmFactory() }

    lateinit var binding: FragmentNotifyBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotifyBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        val adapter = NotifyAdapter(viewModel)

        binding.recyclerNotify.adapter = adapter
        adapter.notifyDataSetChanged()

        viewModel.allLiveEventInvitations.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                invitationValueVisibility(false)
            } else {
                invitationValueVisibility(true)
            }
            adapter.submitList(it)

        })


        return binding.root
    }

    private fun invitationValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValue.visibility = View.GONE
            binding.noValueImage.visibility = View.GONE
        } else {
            binding.noValue.visibility = View.VISIBLE
            binding.noValueImage.visibility = View.VISIBLE
        }
    }
}