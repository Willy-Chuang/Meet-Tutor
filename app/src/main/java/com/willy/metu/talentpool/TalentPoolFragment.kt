package com.willy.metu.talentpool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentTalentpoolBinding

class TalentPoolFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentTalentpoolBinding.inflate(inflater, container, false)

        binding.buttonAddArticle.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToPostArticleDialog())
        }

        return binding.root

    }

}