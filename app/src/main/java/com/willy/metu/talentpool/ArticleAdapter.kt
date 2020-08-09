package com.willy.metu.talentpool

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.willy.metu.MeTuApplication
import com.willy.metu.R

class ArticleAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> AllArticlesFragment()
            else -> MyArticlesFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> MeTuApplication.appContext.getString(R.string.pager_title_all)
            else -> MeTuApplication.appContext.getString(R.string.pager_title_my)
        }
    }
}