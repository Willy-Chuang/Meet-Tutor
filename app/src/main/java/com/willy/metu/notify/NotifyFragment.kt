package com.willy.metu.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.databinding.FragmentNotifyBinding
import com.willy.metu.ext.getVmFactory

class NotifyFragment : Fragment() {

    private val viewModel by viewModels<NotifyViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNotifyBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        val adapter = NotifyAdapter(viewModel)

        binding.recyclerNotify.adapter = adapter
        binding.recyclerNotify.layoutManager = LinearLayoutManager(context)

        viewModel.allLiveEventInvitations.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                binding.noValue.visibility = View.VISIBLE
                binding.noValueImage.visibility = View.VISIBLE
            } else {
                adapter.submitList(it)
            }

        })


        return binding.root
    }
}