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
import com.willy.metu.ext.sortByType
import com.willy.metu.util.Logger

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


        val allType = binding.chipAll
        val studyGroup = binding.chipStudy
        val student = binding.chipStudent
        val tutor = binding.chipTutor

        allType.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.selectedType.value = allType.text.toString()
            }
        }

        studyGroup.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.selectedType.value = studyGroup.text.toString()
            }
        }

        student.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.selectedType.value = student.text.toString()
            }
        }

        tutor.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.selectedType.value = tutor.text.toString()
            }
        }




        viewModel.allLiveArticles.observe(viewLifecycleOwner, Observer {

            viewModel.selectedType.observe(viewLifecycleOwner, Observer {type ->

                if(type == "All Type") {
                    adapter.submitList(it)
                } else {
                    adapter.submitList(it.sortByType(type))
                }

            })

            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.isAdded.observe(viewLifecycleOwner, Observer {
            Logger.d(it.toString())
        })





        return binding.root

    }

}