package com.willy.metu.pair

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.willy.metu.data.User
import com.willy.metu.databinding.ItemProfileCardBinding

class PairingResultAdapter(val viewModel: PairingResultViewModel) : ListAdapter<User, RecyclerView.ViewHolder>(DiffCallback){

    class UserViewHolder(private val binding: ItemProfileCardBinding): RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        fun bind(user: User, viewModel: PairingResultViewModel) {
            binding.lifecycleOwner = this

            binding.user = user

            //Setup chips to show user's tags
            val chipGroup = binding.chipGroup

            val genres = user.tag

            for (genre in genres) {
                val chip = Chip(chipGroup.getContext())
                chip.text = genre
                chipGroup.addView(chip)
            }

            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
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
            ITEM_VIEW_TYPE_USER -> UserViewHolder(ItemProfileCardBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is UserViewHolder -> {
                holder.bind((getItem(position) as User),viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_USER
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is UserViewHolder -> holder.onAttach()
        }
    }

    /**
     * It for [LifecycleRegistry] change [onViewDetachedFromWindow]
     */
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        when (holder) {
            is UserViewHolder -> holder.onDetach()
        }
    }

}