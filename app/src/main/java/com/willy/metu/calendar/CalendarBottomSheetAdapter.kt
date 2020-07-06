package com.willy.metu.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.data.SelectedEvent
import com.willy.metu.databinding.ItemScheduleBinding

class CalendarBottomSheetAdapter() : ListAdapter<SelectedEvent, RecyclerView.ViewHolder>(DiffCallback){

    class EventViewHolder(private var binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(selectedEvent: SelectedEvent) {
            binding.event = selectedEvent
            binding.executePendingBindings()
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<SelectedEvent>() {
        override fun areItemsTheSame(oldItem: SelectedEvent, newItem: SelectedEvent): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: SelectedEvent, newItem: SelectedEvent): Boolean {
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
                holder.bind((getItem(position) as SelectedEvent))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_EVENT
    }
}