package com.willy.metu.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.data.ChatRoom
import com.willy.metu.data.UserInfo
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

        val adapter = ChatListAdapter()
        binding.recyclerChatList.adapter = adapter

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToNewChat())
        }

        viewModel.allLiveChatRooms.observe(viewLifecycleOwner, Observer {
            it.let {
                val filteredChatRoom = mutableListOf<ChatRoom>()

                it.forEach {chatRoom ->

                    // Remove my info to make the new info list contains only the other user's info
                    chatRoom.attendeesInfo = excludeMyInfo(chatRoom.attendeesInfo)

                    filteredChatRoom.add(chatRoom)
                }

                viewModel.createFilteredChatRooms(filteredChatRoom)

            }
        })


        viewModel.filteredChatRooms.observe(viewLifecycleOwner, Observer {
            Logger.w("viewModel.filteredChatRooms.observe, it=$it")
            it?.let {

                binding.recyclerChatList.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_animation)

                if (it.isEmpty()) {
                    binding.noValue.visibility = View.VISIBLE
                    binding.noValueImage.visibility = View.VISIBLE
                } else {
                    adapter.submitList(it)
                }
            }
        })



        return binding.root
    }

    private fun excludeMyInfo (attendeesInfo: List<UserInfo>) : List<UserInfo>{
        return attendeesInfo.filter {
            it.userEmail != UserManager.user.email
        }
    }

}