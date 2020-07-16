package com.willy.metu.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.willy.metu.R
import com.willy.metu.data.ChatRoom
import com.willy.metu.data.Message
import com.willy.metu.databinding.ItemMessageBinding

class ChatRoomAdapter () : ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback){
    class ChatRoomViewHolder(private var binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){

            binding.message = message

            binding.executePendingBindings()


        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_EVENT = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EVENT -> ChatRoomViewHolder(ItemMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is ChatRoomViewHolder -> {
                holder.bind((getItem(position) as Message))
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