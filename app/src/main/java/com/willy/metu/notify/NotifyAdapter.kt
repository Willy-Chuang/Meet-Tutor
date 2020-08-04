package com.willy.metu.notify

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.NavigationDirections
import com.willy.metu.calendar.CalendarBottomSheetAdapter
import com.willy.metu.data.Event
import com.willy.metu.databinding.ItemNotifyEventBinding
import com.willy.metu.login.UserManager

class NotifyAdapter( val viewModel: NotifyViewModel) : ListAdapter<Event, RecyclerView.ViewHolder>(CalendarBottomSheetAdapter) {

    class EventViewHolder(private var binding: ItemNotifyEventBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(event: Event, viewModel: NotifyViewModel) {
            binding.event = event

            val string = SpannableStringBuilder()
                    .bold { append(event.creatorName) }
                    .append(" has sent an invitation of event: ")
                    .bold { append(event.title) }

            binding.textCreatorName.text = string

            binding.layoutInfo.setOnClickListener {
                Navigation.createNavigateOnClickListener(NavigationDirections.navigateToEventDetail(event)).onClick(binding.layoutInfo)
            }

            binding.buttonDecline.setOnClickListener {
                viewModel.declineEvent(event, UserManager.user.email)
            }

            binding.buttonAccept.setOnClickListener {
                viewModel.acceptEvent(event, UserManager.user.email, UserManager.user.name)
            }

            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_EVENT = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EVENT -> EventViewHolder(ItemNotifyEventBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is EventViewHolder -> {
                holder.bind((getItem(position) as Event),viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_EVENT
    }


}