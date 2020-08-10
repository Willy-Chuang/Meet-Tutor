package com.willy.metu.talentpool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.willy.metu.databinding.FragmentTalentPoolBinding

class TalentPoolFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        FragmentTalentPoolBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewpagerArticles.let {
                tabsArticles.setupWithViewPager(it)
                it.adapter = ArticleAdapter(childFragmentManager)
                it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsArticles))
            }
            return@onCreateView root
        }
    }

}