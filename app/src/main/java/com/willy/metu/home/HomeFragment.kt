package com.willy.metu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.databinding.FragmentHomeBinding
import com.willy.metu.ext.getVmFactory

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val recommendAdapter = RecommendAdapter()

        binding.viewModel = viewModel
        binding.recyclerRecommendation.adapter = recommendAdapter
        binding.recyclerRecommendation.layoutManager = LinearLayoutManager(context)


        viewModel.recommendUsers.observe(viewLifecycleOwner, Observer {
            recommendAdapter.submitList(it)
        })

        return binding.root
    }
}