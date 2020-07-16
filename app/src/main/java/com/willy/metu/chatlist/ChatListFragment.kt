package com.willy.metu.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.data.ChatRoom
import com.willy.metu.databinding.FragmentChatListBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager
import com.willy.metu.util.Logger

class ChatListFragment : Fragment() {

    private val viewModel by viewModels<ChatListViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatListBinding.inflate(inflater,container,false)

        binding.recyclerChatList.layoutManager = LinearLayoutManager(context)

        val adapter = ChatListAdapter()

        binding.recyclerChatList.adapter = adapter

        viewModel.allLiveChatRooms.observe(viewLifecycleOwner, Observer {
            it.let {
                val filteredChatRoom = mutableListOf<ChatRoom>()

                it.forEach {chatRoom ->

                    val theOtherPersonInfo = chatRoom.attendeesInfo.filter {userInfo ->
                        userInfo.userEmail != UserManager.user.email
                    }
                    chatRoom.attendeesInfo = theOtherPersonInfo

                    filteredChatRoom.add(chatRoom)
                }

                viewModel.filteredChatRooms.value = filteredChatRoom

            }
        })


//        viewModel.allLiveChatRooms.value?.let {
//
//            val filteredChatRoom = mutableListOf<ChatRoom>()
//
//            it.forEach {chatRoom ->
//
//                val theOtherPersonInfo = chatRoom.attendeesInfo.filter {userInfo ->
//                    userInfo.userEmail != UserManager.user.email
//                }
//                chatRoom.attendeesInfo = theOtherPersonInfo
//
//                filteredChatRoom.add(chatRoom)
//            }
//
//            viewModel.filteredChatRooms.value = filteredChatRoom
//
//        }

        viewModel.filteredChatRooms.observe(viewLifecycleOwner, Observer {
            Logger.w(it.toString())
            adapter.submitList(it)
        })



        return binding.root
    }

}