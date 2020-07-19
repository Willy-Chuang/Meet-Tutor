package com.willy.metu.talentpool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.NavigationDirections
import com.willy.metu.databinding.FragmentTalentpoolBinding
import com.willy.metu.ext.getVmFactory

class TalentPoolFragment : Fragment() {

    private val viewModel by viewModels<TalentPoolViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentTalentpoolBinding.inflate(inflater, container, false)
        val adapter = TalentPoolAdapter(viewModel)
        binding.recyclerArticle.adapter = adapter
        binding.recyclerArticle.layoutManager = LinearLayoutManager(context)
        binding.viewModel = viewModel

        binding.buttonAddArticle.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToPostArticleDialog())
        }


        viewModel.allLiveArticles.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        return binding.root

    }

}