package com.willy.metu.followlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.willy.metu.databinding.ItemFollowedUsersBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortToOnlyStudents
import com.willy.metu.ext.sortToOnlyTutors

class ItemFollowedUsersFragment : Fragment() {

    private val viewModel by viewModels<FollowUserViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = ItemFollowedUsersBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.recyclerTutor.layoutManager = LinearLayoutManager(context)
        binding.recyclerStudent.layoutManager = LinearLayoutManager(context)

        val studentAdapter = StudentListAdapter()
        val tutorAdapter = TutorListAdapter()
        binding.recyclerStudent.adapter = studentAdapter
        binding.recyclerTutor.adapter = tutorAdapter

        viewModel.allFollowedList.observe(viewLifecycleOwner, Observer {
            viewModel.followedStudents.value = it.sortToOnlyStudents()
            viewModel.followedTutors.value = it.sortToOnlyTutors()
        })

        viewModel.followedStudents.observe(viewLifecycleOwner, Observer {
            studentAdapter.submitList(it)
        })

        viewModel.followedTutors.observe(viewLifecycleOwner, Observer {
            tutorAdapter.submitList(it)
        })


        return binding.root
    }
}