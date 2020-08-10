package com.willy.metu.followlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.willy.metu.databinding.FragmentFollowlistBinding

class FollowListFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        FragmentFollowlistBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner

            // Setup view pager
            viewpagerFollow.let {
                tabsFollow.setupWithViewPager(it)
                it.adapter = FollowAdapter(childFragmentManager)
                it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsFollow))
            }
            return@onCreateView root
        }
    }
}