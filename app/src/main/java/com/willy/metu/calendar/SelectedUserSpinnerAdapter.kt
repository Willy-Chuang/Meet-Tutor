package com.willy.metu.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.databinding.ItemFollowedUserSpinnerBinding

class SelectedUserSpinnerAdapter (private val strings: Array<String>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemFollowedUserSpinnerBinding.inflate(LayoutInflater.from(parent?.context),parent,false)

        if(position == 0){
            binding.user = MeTuApplication.instance.resources.getString(R.string.spinner_select_member)
            binding.textSpinnerTitle.setTextColor(MeTuApplication.appContext.resources.getColor(R.color.black_12_alpha))

        } else {
            binding.user = strings[position-1]
        }

        return binding.root
    }

    override fun getItem(position: Int): Any {
        return if (position == 0)
            0
        else
            strings[position-1]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return strings.size + 1
    }

}