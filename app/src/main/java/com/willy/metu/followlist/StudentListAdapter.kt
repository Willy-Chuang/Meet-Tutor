package com.willy.metu.followlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.data.User
import com.willy.metu.databinding.ItemFollowBinding

class StudentListAdapter : ListAdapter<User, RecyclerView.ViewHolder>(DiffCallback){
    class UserViewHolder(private var binding: ItemFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.user = user

            //Setup chips to show user's tags
            val chipGroup = binding.chipGroup

            val genres = user.tag

            for (genre in genres) {
                val chip = Chip(chipGroup.context, null, R.attr.CustomChipChoice)
                chip.text = genre
                chipGroup.addView(chip)
            }

            binding.layoutCardUser.setOnClickListener {
                Navigation.createNavigateOnClickListener(NavigationDirections.navigateToUserDetail(user.email)).onClick(binding.layoutCardUser)
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

        private const val ITEM_VIEW_TYPE_USER = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_USER -> UserViewHolder(ItemFollowBinding.inflate(
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
        return ITEM_VIEW_TYPE_USER
    }
}