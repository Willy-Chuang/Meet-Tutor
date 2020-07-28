package com.willy.metu.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.NavigationDirections
import com.willy.metu.data.User
import com.willy.metu.databinding.ItemRecommendUserBinding
import com.willy.metu.login.UserManager

class NewUserAdapter() : ListAdapter<User, RecyclerView.ViewHolder>(DiffCallback) {
    class UserViewHolder(private var binding: ItemRecommendUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user

            binding.userImage.setOnClickListener {
                if (user.email == UserManager.user.email) {
                    Navigation.createNavigateOnClickListener(NavigationDirections.navigateToProfile()).onClick(binding.userImage)
                } else {
                    Navigation.createNavigateOnClickListener(NavigationDirections.navigateToUserDetail(user.email)).onClick(binding.userImage)
                }
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
            ITEM_VIEW_TYPE_EVENT -> UserViewHolder(ItemRecommendUserBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is UserViewHolder -> {
                holder.bind((getItem(position) as User))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_EVENT
    }
}