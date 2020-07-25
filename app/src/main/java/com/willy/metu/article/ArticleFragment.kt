package com.willy.metu.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.willy.metu.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentArticleBinding.inflate(inflater, container,false)


        return binding.root
    }

}