package com.willy.metu.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.MainActivity
import com.willy.metu.MeTuApplication
import com.willy.metu.databinding.FragmentChatroomBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager
import com.willy.metu.util.Logger


class ChatRoomFragment : Fragment() {

    private val viewModel by viewModels<ChatRoomViewModel> {
        getVmFactory(
                ChatRoomFragmentArgs.fromBundle(requireArguments()).userEmail, ChatRoomFragmentArgs.fromBundle(requireArguments()).userName
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentChatroomBinding.inflate(inflater, container, false)
        val adapter = ChatRoomAdapter()
        binding.viewModel = viewModel
        binding.recyclerMessage.adapter = adapter
        binding.recyclerMessage.layoutManager = LinearLayoutManager(context)

        val myUserEmail = UserManager.user.email
        val friendUserEmail = viewModel.currentChattingUser

        viewModel.enterMessage.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        binding.buttonSendText.setOnClickListener {
            if (viewModel.enterMessage.value == null) {
                Toast.makeText(MeTuApplication.appContext, " Please send something", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.postMessage(viewModel.getUserEmails(myUserEmail, friendUserEmail), viewModel.getMessage())
                binding.editMessage.text.clear()
            }
        }

        viewModel.allLiveMessage.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it)

        })

        if (activity is MainActivity) {
            (activity as MainActivity).setSupportActionBar(binding.toolbar)
        }

        return binding.root

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return false
    }
}