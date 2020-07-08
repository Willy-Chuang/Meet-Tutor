package com.willy.metu.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.data.Event
import com.willy.metu.databinding.ItemScheduleBinding

class CalendarBottomSheetAdapter() : ListAdapter<Event, RecyclerView.ViewHolder>(DiffCallback){

    class EventViewHolder(private var binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(Event: Event) {
            binding.event = Event
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
            ITEM_VIEW_TYPE_EVENT -> EventViewHolder(ItemScheduleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is EventViewHolder -> {
                holder.bind((getItem(position) as Event))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_EVENT
    }
}