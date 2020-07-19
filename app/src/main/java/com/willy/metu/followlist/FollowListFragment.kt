package com.willy.metu.followlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.willy.metu.databinding.FragmentFollowlistBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortToOnlyStudents
import com.willy.metu.ext.sortToOnlyTutors
import com.willy.metu.util.Logger

class FollowListFragment : Fragment() {

    private val viewModel by viewModels<FollowListViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        FragmentFollowlistBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewpagerFollow.let {
                tabsFollow.setupWithViewPager(it)
                it.adapter = FollowAdapter(childFragmentManager)
                it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsFollow))
            }
            return@onCreateView root
        }
    }
}