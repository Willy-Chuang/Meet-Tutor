package com.willy.metu.newchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentNewChatBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortByName

class NewChatFragment : Fragment() {

    private val viewModel by viewModels<NewChatViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewChatBinding.inflate(inflater, container, false)
        val adapter = NewChatAdapter()

        binding.viewModel = viewModel
        binding.recyclerFollowList.layoutManager = LinearLayoutManager(context)
        binding.recyclerFollowList.adapter = adapter




        viewModel.allFollowedList.observe(viewLifecycleOwner, Observer { all ->
            all?.let { users ->

                if (users.isEmpty()) {
                    binding.noValue.visibility = View.VISIBLE
                    binding.noValueButton.visibility = View.VISIBLE
                    binding.noValueButton.setOnClickListener {
                        findNavController().navigate(NavigationDirections.navigateToPairingFragment())
                    }
                } else {
                    binding.noValue.visibility =View.GONE
                    binding.noValue.visibility = View.GONE
                }

                adapter.submitList(users)

                viewModel.searchBy.observe(viewLifecycleOwner, Observer {

                    val sortedList = users.sortByName(it)

                    if (sortedList.isEmpty()) {
                        binding.noValue.visibility = View.VISIBLE
                        binding.noValueButton.visibility = View.VISIBLE
                        binding.noValueButton.setOnClickListener {
                            findNavController().navigate(NavigationDirections.navigateToPairingFragment())
                        }
                    } else {
                        binding.noValue.visibility =View.GONE
                        binding.noValue.visibility = View.GONE
                    }

                    adapter.submitList(sortedList)

                })
            }
        })

        return binding.root

    }
}