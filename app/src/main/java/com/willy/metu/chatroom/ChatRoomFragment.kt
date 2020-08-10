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
import com.willy.metu.R
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

    lateinit var binding: FragmentChatroomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatroomBinding.inflate(inflater, container, false)
        val adapter = ChatRoomAdapter()
        binding.viewModel = viewModel
        binding.recyclerMessage.adapter = adapter
        binding.recyclerMessage.layoutManager = LinearLayoutManager(context)

        val myUserEmail = UserManager.user.email
        val friendUserEmail = viewModel.currentChattingUser

        // Setup custom toolbar
        if (activity is MainActivity) {
            (activity as MainActivity).setSupportActionBar(binding.toolbar)
        }

        binding.buttonSendText.setOnClickListener {
            if (isEmpty()) {
                Toast.makeText(MeTuApplication.appContext, getString(R.string.reminder_chatroom_message), Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(myUserEmail, friendUserEmail)
            }
        }

        // Observers
        viewModel.allLiveMessage.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.enterMessage.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        return binding.root

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return false
    }

    fun isEmpty(): Boolean {
        return when (viewModel.enterMessage.value) {
            null -> true
            else -> false
        }
    }

    private fun sendMessage(myEmail: String, friendEmail: String) {
        viewModel.postMessage(viewModel.getUserEmails(myEmail, friendEmail), viewModel.getMessage())
        binding.editMessage.text.clear()
    }


}