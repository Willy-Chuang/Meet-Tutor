package com.willy.metu.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.bindImage
import com.willy.metu.data.ChatRoom
import com.willy.metu.databinding.ItemChatListBinding

class ChatListAdapter () : ListAdapter<ChatRoom, RecyclerView.ViewHolder>(DiffCallback){
    class ChatRoomViewHolder(private var binding: ItemChatListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(chatRoom: ChatRoom){

            binding.chatRoom = chatRoom

            val friendInfo = chatRoom.attendeesInfo.component1()

            binding.textChatName.text = friendInfo.userName
            binding.imageUrl = friendInfo.userImage

            binding.layoutChatList.setOnClickListener{
                Navigation.createNavigateOnClickListener(NavigationDirections.navigateToChatRoom(friendInfo.userEmail, friendInfo.userName)).onClick(binding.layoutChatList)
            }
            binding.executePendingBindings()


        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.chatRoomId == newItem.chatRoomId
        }

        private const val ITEM_VIEW_TYPE_EVENT = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EVENT -> ChatRoomViewHolder(ItemChatListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is ChatRoomViewHolder -> {
                holder.bind((getItem(position) as ChatRoom))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_EVENT
    }

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = it.toUri().buildUpon().build()
            Glide.with(imgView.context)
                    .load(imgUri)
                    .apply(
                            RequestOptions()
                                    .placeholder(R.drawable.ic_face_black_24)
                                    .error(R.drawable.ic_face_black_24))
                    .into(imgView)
        }
    }


}