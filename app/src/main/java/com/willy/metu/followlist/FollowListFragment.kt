package com.willy.metu.followlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.willy.metu.databinding.FragmentFollowlistBinding

class FollowListFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFollowlistBinding.inflate(inflater, container, false)


        return binding.root
    }
}