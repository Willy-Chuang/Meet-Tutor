package com.willy.metu.newchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentNewChatBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortByName

class NewChatFragment : Fragment() {

    private val viewModel by viewModels<NewChatViewModel> { getVmFactory() }

    lateinit var binding: FragmentNewChatBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewChatBinding.inflate(inflater, container, false)
        val adapter = NewChatAdapter()

        binding.viewModel = viewModel
        binding.recyclerFollowList.adapter = adapter


        viewModel.allFollowedList.observe(viewLifecycleOwner, Observer { all ->
            all?.let { users ->

                if (users.isEmpty()) {
                    chatListValueVisibility(false)
                } else {
                    chatListValueVisibility(true)
                }

                adapter.submitList(users)

            }
        })

        // Filter list based on the input of edit text
        viewModel.searchBy.observe(viewLifecycleOwner, Observer {

            val sortedList = viewModel.allFollowedList.value.sortByName(it)

            if (sortedList.isEmpty()) {
                chatListValueVisibility(false)
            } else {
                chatListValueVisibility(true)
            }

            adapter.submitList(sortedList)

        })

        return binding.root

    }

    private fun chatListValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValue.visibility = View.GONE
            binding.noValueButton.visibility = View.GONE
        } else {
            binding.noValue.visibility = View.VISIBLE
            binding.noValueButton.visibility = View.VISIBLE
            binding.noValueButton.setOnClickListener {
                findNavController().navigate(NavigationDirections.navigateToPairingFragment())
            }
        }
    }
}