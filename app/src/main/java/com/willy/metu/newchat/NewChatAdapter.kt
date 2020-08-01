package com.willy.metu.newchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.NavigationDirections
import com.willy.metu.data.User
import com.willy.metu.databinding.ItemNewChatBinding

class NewChatAdapter () : ListAdapter<User, RecyclerView.ViewHolder>(DiffCallback){
    class UserViewHolder(private var binding: ItemNewChatBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(User: User){
            binding.user = User

            binding.layoutCardUser.setOnClickListener {
                Navigation.createNavigateOnClickListener(NavigationDirections.navigateToChatRoom(User.email,User.name)).onClick(binding.layoutCardUser)
            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_EVENT = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EVENT -> UserViewHolder(ItemNewChatBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is UserViewHolder -> {
                holder.bind((getItem(position) as User))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_EVENT
    }
}